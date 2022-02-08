package oembed.test.service;

import lombok.RequiredArgsConstructor;
import oembed.test.api.ApiClient;
import oembed.test.dto.OembedProviderRes;
import org.springframework.stereotype.Service;
import oembed.test.dto.OembedProvider.EndPoint;

@Service
@RequiredArgsConstructor
public class OembedService {
    private final ApiClient apiClient;

    public OembedProviderRes requestOembedDataByUrl(String url) {
        return apiClient.requestOembedProviders()
                .stream()
                .filter(oembedProvider -> {
                    for (EndPoint endPoint : oembedProvider.endpoints) {
                        if (endPoint.schemes == null) return false;
                        for (String scheme : endPoint.schemes) {
                            if (url.matches(scheme.replace("*", ".*"))) {
                                return true;
                            }
                        }
                    }
                    return false;
                })
                .map(oembedProvider -> {
                    return apiClient.requestOembedDataToProvider(
                            oembedProvider
                                    .endpoints.get(0).url
                                    .replace("{format}", "json") + "?url=" + url);
                })
                .findFirst()
                .orElse(new OembedProviderRes());
    }

}
