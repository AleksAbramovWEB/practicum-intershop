package ru.abramov.practicum.pay.api;


import jakarta.annotation.Generated;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.abramov.practicum.pay.handler.AccountHandler;
import ru.abramov.practicum.pay.model.PaymentRequest;
import ru.abramov.practicum.pay.model.PaymentResponse;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-08T15:44:49.285739+03:00[Europe/Moscow]", comments = "Generator version: 7.12.0")
@RestController
@AllArgsConstructor
public class PayApiController implements PayApi {

    private final AccountHandler accountHandler;

    @Override
    public Mono<ResponseEntity<PaymentResponse>> payPost(Mono<PaymentRequest> paymentRequest, ServerWebExchange exchange) {
        return paymentRequest.flatMap(request -> accountHandler.payment(request.getUserId(), request.getAmount())
                .thenReturn(ResponseEntity.ok(new PaymentResponse(Boolean.TRUE))));
    }
}
