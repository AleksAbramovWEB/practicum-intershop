package ru.abramov.practicum.intershop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.abramov.practicum.intershop.client.pay.api.PayApi;
import ru.abramov.practicum.intershop.client.pay.invoker.ApiClient;

@Configuration
public class PayApiConfig {

    @Value("${service.pay.host}")
    private String host;

    @Value("${service.pay.port}")
    private String port;

    @Bean
    public PayApi payApi() {

        ApiClient apiClient = new ApiClient();

        apiClient.setBasePath(host + ":" + port);

        return new PayApi(apiClient);
    }
}
