package ru.abramov.practicum.pay.api;


import jakarta.annotation.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.pay.handler.AccountHandler;
import ru.abramov.practicum.pay.model.BalanceRequest;
import ru.abramov.practicum.pay.model.BalanceResponse;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-08T15:44:49.285739+03:00[Europe/Moscow]", comments = "Generator version: 7.12.0")
@RestController
@RequiredArgsConstructor
public class BalanceApiController implements BalanceApi {

    private final AccountHandler accountHandler;

    @Override
    public Mono<ResponseEntity<BalanceResponse>> balanceGet(String userId, ServerWebExchange exchange) {
        return accountHandler.getBalance(userId)
                .map(balance -> {
                    BalanceResponse balanceResponse = new BalanceResponse();
                    balanceResponse.setBalance(balance);

                    return ResponseEntity.ok(balanceResponse);
                });
    }
}
