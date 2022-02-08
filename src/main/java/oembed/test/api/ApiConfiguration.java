package oembed.test.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Configuration
public class ApiConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();

        template.getInterceptors().add((request, body, execution) -> {
            ClientHttpResponse response = execution.execute(request, body);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response;
        });

        return template;
    }

    @Bean
    public Logger log(){
        return Logger.getGlobal();
    }

}
