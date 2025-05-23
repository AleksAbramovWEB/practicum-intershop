package ru.abramov.practicum.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClientConfigurer;

import java.util.List;

@SpringBootTest
@AutoConfigureWebTestClient
class AbstractApiTest {

    @Autowired
    protected WebTestClient webTestClient;

    protected WebTestClientConfigurer getMockJwt() {
        return SecurityMockServerConfigurers.mockJwt()
                .jwt(jwt -> jwt
                        .claim("sub", "user-42")
                        .claim("email", "user@demo.com")
                        .claim("roles", List.of("ROLE_USER"))
                );
    }
}
