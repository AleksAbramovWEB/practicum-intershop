package ru.abramov.practicum.intershop.client.pay.api;

import ru.abramov.practicum.intershop.client.pay.invoker.ApiClient;

import ru.abramov.practicum.intershop.client.pay.domain.BalanceResponse;
import ru.abramov.practicum.intershop.client.pay.domain.PaymentRequest;
import ru.abramov.practicum.intershop.client.pay.domain.PaymentResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-05-09T09:47:00.861209+03:00[Europe/Moscow]", comments = "Generator version: 7.12.0")
public class PayApi {
    private ApiClient apiClient;

    public PayApi() {
        this(new ApiClient());
    }

    public PayApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    
    /**
     * Получение баланса
     * 
     * <p><b>200</b> - Баланс успешно получен
     * @return BalanceResponse
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec balanceGetRequestCreation(String userId) throws WebClientResponseException {
        Object postBody = null;

        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();

        queryParams.add("user_id", userId);

        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<BalanceResponse> localVarReturnType = new ParameterizedTypeReference<BalanceResponse>() {};
        return apiClient.invokeAPI("/balance", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Получение баланса
     * 
     * <p><b>200</b> - Баланс успешно получен
     * @return BalanceResponse
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<BalanceResponse> balanceGet(String userId) throws WebClientResponseException {
        ParameterizedTypeReference<BalanceResponse> localVarReturnType = new ParameterizedTypeReference<BalanceResponse>() {};
        return balanceGetRequestCreation(userId).bodyToMono(localVarReturnType);
    }

    /**
     * Получение баланса
     * 
     * <p><b>200</b> - Баланс успешно получен
     * @return ResponseEntity&lt;BalanceResponse&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<BalanceResponse>> balanceGetWithHttpInfo(String userId) throws WebClientResponseException {
        ParameterizedTypeReference<BalanceResponse> localVarReturnType = new ParameterizedTypeReference<BalanceResponse>() {};
        return balanceGetRequestCreation(userId).toEntity(localVarReturnType);
    }

    /**
     * Получение баланса
     * 
     * <p><b>200</b> - Баланс успешно получен
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec balanceGetWithResponseSpec(String userId) throws WebClientResponseException {
        return balanceGetRequestCreation(userId);
    }

    /**
     * Выполнение платежа
     * 
     * <p><b>200</b> - Платёж успешен
     * <p><b>400</b> - Недостаточно средств
     * @param paymentRequest The paymentRequest parameter
     * @return PaymentResponse
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec payPostRequestCreation(PaymentRequest paymentRequest) throws WebClientResponseException {
        Object postBody = paymentRequest;
        // verify the required parameter 'paymentRequest' is set
        if (paymentRequest == null) {
            throw new WebClientResponseException("Missing the required parameter 'paymentRequest' when calling payPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<PaymentResponse> localVarReturnType = new ParameterizedTypeReference<PaymentResponse>() {};
        return apiClient.invokeAPI("/pay", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Выполнение платежа
     * 
     * <p><b>200</b> - Платёж успешен
     * <p><b>400</b> - Недостаточно средств
     * @param paymentRequest The paymentRequest parameter
     * @return PaymentResponse
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<PaymentResponse> payPost(PaymentRequest paymentRequest) throws WebClientResponseException {
        ParameterizedTypeReference<PaymentResponse> localVarReturnType = new ParameterizedTypeReference<PaymentResponse>() {};
        return payPostRequestCreation(paymentRequest).bodyToMono(localVarReturnType);
    }

    /**
     * Выполнение платежа
     * 
     * <p><b>200</b> - Платёж успешен
     * <p><b>400</b> - Недостаточно средств
     * @param paymentRequest The paymentRequest parameter
     * @return ResponseEntity&lt;PaymentResponse&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<PaymentResponse>> payPostWithHttpInfo(PaymentRequest paymentRequest) throws WebClientResponseException {
        ParameterizedTypeReference<PaymentResponse> localVarReturnType = new ParameterizedTypeReference<PaymentResponse>() {};
        return payPostRequestCreation(paymentRequest).toEntity(localVarReturnType);
    }

    /**
     * Выполнение платежа
     * 
     * <p><b>200</b> - Платёж успешен
     * <p><b>400</b> - Недостаточно средств
     * @param paymentRequest The paymentRequest parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec payPostWithResponseSpec(PaymentRequest paymentRequest) throws WebClientResponseException {
        return payPostRequestCreation(paymentRequest);
    }
}
