package ru.abramov.practicum.pay.handler;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface AccountHandler {

    Mono<BigDecimal> getBalance();

    Mono<Void> payment(BigDecimal amount);
}
