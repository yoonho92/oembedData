package oembed.test.api;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import oembed.test.dto.OembedProvider;
import oembed.test.dto.OembedProviderRes;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class ApiClient {

    private final RestTemplate template;
    private final Logger log;

    public List<OembedProvider> requestOembedProviders() {
        return template.exchange(
                "https://oembed.com/providers.json",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ArrayList<OembedProvider>>() {}
        ).getBody();
    }

    public OembedProviderRes requestOembedDataToProvider(String url) {
        try {
            return template.getForObject(url, OembedProviderRes.class);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return new OembedProviderRes(e.getMessage());
        }
    }

}
