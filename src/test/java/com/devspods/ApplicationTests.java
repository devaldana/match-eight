package com.devspods;

import com.devspods.services.NbaDataService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.*;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.Assert.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ApplicationTests {

    @Autowired
    private NbaDataService nbaDataService;

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public RestTemplateBuilder restTemplateBuilder() throws IOException {
            var restTemplate = new RestTemplate();
            var mockServer = MockRestServiceServer.createServer(restTemplate);
            mockServer.expect(requestTo("https://devspods.com"))
                      .andExpect(method(HttpMethod.GET))
                      .andRespond(withSuccess(getTestData(), APPLICATION_JSON));

            var builder = mock(RestTemplateBuilder.class);
            when(builder.build()).thenReturn(restTemplate);
            return builder;
        }

        private String getTestData() throws IOException {
            return Files.readString(Paths.get("src", "test", "resources", "data.json"));
        }
    }

    @Test
    void getPairs() {
        var pairs = nbaDataService.getPairs(139);
        notNull(pairs, "Pairs should NOT be null");
        isTrue(pairs.size() == 2, "Should be 2");

        for (var pair : pairs) {
            if (pair.getPlayer1().getFullName().equals("Nate Robinson")
                    && pair.getPlayer2().getFullName().equals("Brevin Knight")) {
                isTrue(pair.toString().startsWith("69\" - Nate Robinson             70\" - Brevin Knight"), "Should match");
            } else {
                isTrue(pair.toString().startsWith("69\" - Nate Robinson             70\" - Mike Wilks"), "Should match");
            }
            isTrue(pair.getPlayer1().getHeight() == 69, "Should be 69");
            isTrue(pair.getPlayer2().getHeight() == 70, "Should be 70");
        }
    }
}
