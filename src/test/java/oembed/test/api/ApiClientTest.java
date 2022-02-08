package oembed.test.api;

import oembed.test.dto.OembedProvider;
import oembed.test.dto.OembedProviderRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;



@SpringBootTest
class ApiClientTest {

    @Autowired
    private ApiClient apiClient;

    @Autowired
    RestTemplate template;
    MockRestServiceServer server;

    String providers = "[\n" +
            "  {\n" +
            "    \"provider_name\": \"YouTube\",\n" +
            "    \"provider_url\": \"https://www.youtube.com/\",\n" +
            "    \"endpoints\": [\n" +
            "      {\n" +
            "        \"schemes\": [\n" +
            "          \"https://*.youtube.com/watch*\",\n" +
            "          \"https://*.youtube.com/v/*\",\n" +
            "          \"https://youtu.be/*\",\n" +
            "          \"https://*.youtube.com/playlist?list=*\",\n" +
            "          \"https://youtube.com/playlist?list=*\"\n" +
            "        ],\n" +
            "        \"url\": \"https://www.youtube.com/oembed\",\n" +
            "        \"discovery\": true\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]";

    String providerRes = "{\n" +
            "  \"type\": \"video\",\n" +
            "  \"version\": \"1.0\",\n" +
            "  \"provider_name\": \"Vimeo\",\n" +
            "  \"provider_url\": \"https://vimeo.com/\",\n" +
            "  \"title\": \"A Time Artifact - work in progress\",\n" +
            "  \"author_name\": \"Depict_tk\",\n" +
            "  \"author_url\": \"https://vimeo.com/user5346842\",\n" +
            "  \"is_plus\": \"0\",\n" +
            "  \"account_type\": \"basic\",\n" +
            "  \"html\": \"<iframe src=\\\"https://player.vimeo.com/video/20097015?h=08095da358&amp;app_id=122963\\\" width=\\\"640\\\" height=\\\"480\\\" frameborder=\\\"0\\\" allow=\\\"autoplay; fullscreen; picture-in-picture\\\" allowfullscreen title=\\\"A Time Artifact - work in progress\\\"></iframe>\",\n" +
            "  \"width\": 640,\n" +
            "  \"height\": 480,\n" +
            "  \"duration\": 314,\n" +
            "  \"description\": \"concepts: playzoo\\ncodes: purpleworks\\n\\nopenFrameworks (Linux) / Logitech HD webcam / Turntable\",\n" +
            "  \"thumbnail_url\": \"https://i.vimeocdn.com/video/128068226-3e88e664fc0805498112c738d3a44fe6319a6385f63e3a2af526b35440a15ed5-d_640\",\n" +
            "  \"thumbnail_width\": 640,\n" +
            "  \"thumbnail_height\": 480,\n" +
            "  \"thumbnail_url_with_play_button\": \"https://i.vimeocdn.com/filter/overlay?src0=https%3A%2F%2Fi.vimeocdn.com%2Fvideo%2F128068226-3e88e664fc0805498112c738d3a44fe6319a6385f63e3a2af526b35440a15ed5-d_640&src1=http%3A%2F%2Ff.vimeocdn.com%2Fp%2Fimages%2Fcrawler_play.png\",\n" +
            "  \"upload_date\": \"2011-02-18 05:31:51\",\n" +
            "  \"video_id\": 20097015,\n" +
            "  \"uri\": \"/videos/20097015\"\n" +
            "}";

    @BeforeEach
    private void before() {
        server = MockRestServiceServer.createServer(template);
    }

    @Test
    @DisplayName("provider 목록을 불러오다")
    void requestOembedProviders() {
        server.expect(requestTo("https://oembed.com/providers.json"))
                .andRespond(withSuccess(providers, MediaType.TEXT_PLAIN));

        List<OembedProvider> respond = apiClient.requestOembedProviders();

        assertThat(respond.get(0).provider_name).isEqualTo("YouTube");
        assertThat(respond.get(0).endpoints.get(0).schemes).contains("https://*.youtube.com/watch*");
        assertThat(respond.get(0).endpoints.get(0).url).isEqualTo("https://www.youtube.com/oembed");
    }

    @Test
    @DisplayName("oembed 데이터를 불러오다")
    void requestOembedDataToProviderByUrl() {
        String url = "https://vimeo.com/api/oembed.json?url=https://vimeo.com/20097015";
        server.expect(requestTo(url))
                .andRespond(withSuccess(providerRes, MediaType.APPLICATION_JSON));

        OembedProviderRes respond = apiClient.requestOembedDataToProvider(url);

        assertThat(respond.type).isEqualTo("video");
        assertThat(respond.provider_name).isEqualTo("Vimeo");
        assertThat(respond.width).isEqualTo(640);
    }

    @Test
    @DisplayName("올바르지 않은 url로 oembed 데이터를 불러오다")
    void requestOembedDataToProviderByErrorUrl() {
        String url = "https://vimeo.com/api/oembed.json?url=https://vimeo.com/20097015";
        server.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        OembedProviderRes respond = apiClient.requestOembedDataToProvider(url);

        assertThat(respond.type).isBlank();
        assertThat(respond.provider_name).isBlank();
        assertThat(respond.width).isEqualTo(0);
    }

}