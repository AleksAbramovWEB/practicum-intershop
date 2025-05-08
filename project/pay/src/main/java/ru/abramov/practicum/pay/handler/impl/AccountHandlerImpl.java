package ru.abramov.practicum.pay.handler.impl;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.pay.handler.AccountHandler;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class AccountHandlerImpl implements AccountHandler {

    private final AtomicReference<BigDecimal> balance = new AtomicReference<>(BigDecimal.valueOf(100000));

    @Override
    public Mono<BigDecimal> getBalance() {
        return Mono.just(balance.get());
    }

    @Override
    public Mono<Void> payment(BigDecimal amount) {
        return Mono.fromRunnable(() -> {
            while (true) {
                BigDecimal current = balance.get();
                if (current.compareTo(amount) < 0) {
                    throw new IllegalStateException("There are insufficient funds in the account");
                }
                BigDecimal updated = current.subtract(amount);
                if (balance.compareAndSet(current, updated)) {
                    return;
                }
            }
        });
    }
}
