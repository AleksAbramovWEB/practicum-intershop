package ru.abramov.practicum.pay.handler.impl;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.pay.handler.AccountHandler;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class AccountHandlerImpl implements AccountHandler {

    private static final BigDecimal DEFAULT_BALANCE = BigDecimal.valueOf(100_000);
    private final ConcurrentHashMap<String, AtomicReference<BigDecimal>> balances = new ConcurrentHashMap<>();

    @Override
    public Mono<BigDecimal> getBalance(String userId) {
        return Mono.fromSupplier(() ->
                balances.computeIfAbsent(userId, id -> new AtomicReference<>(DEFAULT_BALANCE)).get()
        );
    }

    @Override
    public Mono<Void> payment(String userId, BigDecimal amount) {
        return Mono.fromRunnable(() -> {
            AtomicReference<BigDecimal> balanceRef = balances.computeIfAbsent(userId, id -> new AtomicReference<>(DEFAULT_BALANCE));

            while (true) {
                BigDecimal current = balanceRef.get();
                if (current.compareTo(amount) < 0) {
                    throw new IllegalStateException("There are insufficient funds in the account");
                }
                BigDecimal updated = current.subtract(amount);
                if (balanceRef.compareAndSet(current, updated)) {
                    return;
                }
            }
        });
    }
}
