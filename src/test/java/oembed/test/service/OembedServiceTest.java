package oembed.test.service;

import oembed.test.api.ApiClient;
import oembed.test.dto.OembedProvider;
import oembed.test.dto.OembedProviderRes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class OembedServiceTest {

    @Mock
    private ApiClient apiClient;

    @InjectMocks
    private OembedService service;

    @Test
    @DisplayName("Oembed 데이터를 불러오다")
    void requestOembedDataByUrl() {
        OembedProvider op = new OembedProvider();
        OembedProvider.EndPoint ep = new OembedProvider.EndPoint();

        ep.setUrl("https://vimeo.com/api/oembed.{format}");
        ep.setDiscovery("true");
        ep.setSchemes(Arrays.asList("https://vimeo.com/*",
                "https://vimeo.com/album/*/video/*",
                "https://vimeo.com/channels/*/*",
                "https://vimeo.com/groups/*/videos/*",
                "https://vimeo.com/ondemand/*/*",
                "https://player.vimeo.com/video/*"));

        op.setProvider_name("Vimeo");
        op.setProvider_url("https://vimeo.com/");
        op.setEndpoints(Arrays.asList(ep));

        OembedProviderRes opr = new OembedProviderRes();
        opr.setType("video");
        opr.setVersion("1.0");
        opr.setProvider_name("Vimeo");
        opr.setProvider_url("https://vimeo.com/");
        opr.setTitle("A Time Artifact - work in progress");
        opr.setAuthor_name("Depict_tk");
        opr.setAuthor_url("https://vimeo.com/user5346842");
        opr.setHtml("<iframe src=\\\"https://player.vimeo.com/video/20097015?h=08095da358&amp;app_id=122963\\\" width=\\\"640\\\" height=\\\"480\\\" frameborder=\\\"0\\\" allow=\\\"autoplay; fullscreen; picture-in-picture\\\" allowfullscreen title=\\\"A Time Artifact - work in progress\\\"></iframe>");
        opr.setWidth(640);
        opr.setHeight(480);
        opr.setThumbnail_url("https://i.vimeocdn.com/filter/overlay?src0=https%3A%2F%2Fi.vimeocdn.com%2Fvideo%2F128068226-3e88e664fc0805498112c738d3a44fe6319a6385f63e3a2af526b35440a15ed5-d_640&src1=http%3A%2F%2Ff.vimeocdn.com%2Fp%2Fimages%2Fcrawler_play.png");
        opr.setThumbnail_width(640);
        opr.setHeight(480);
        opr.setUrl("/videos/20097015");

        String requestUrl = "https://vimeo.com/20097015";
        String mappedUrl = "https://vimeo.com/api/oembed.json?url=https://vimeo.com/20097015";

        given(apiClient.requestOembedProviders()).willReturn(List.of(op));
        given(apiClient.requestOembedDataToProvider(mappedUrl)).willReturn(opr);

        OembedProviderRes respond = service.requestOembedDataByUrl(requestUrl);

        assertThat(respond.type).isEqualTo("video");
        assertThat(respond.provider_name).isEqualTo("Vimeo");
        assertThat(respond.width).isEqualTo(640);
    }

    @Test
    @DisplayName("provider에 존재하지 않는 url로 oembed 데이터를 불러오다")
    void requestOembedDataByErrorUrl() {
        OembedProvider op = new OembedProvider();
        OembedProvider.EndPoint ep = new OembedProvider.EndPoint();

        ep.setUrl("https://vimeo.com/api/oembed.{format}");
        ep.setDiscovery("true");
        ep.setSchemes(Arrays.asList("https://vimeo.com/*",
                "https://vimeo.com/album/*/video/*",
                "https://vimeo.com/channels/*/*",
                "https://vimeo.com/groups/*/videos/*",
                "https://vimeo.com/ondemand/*/*",
                "https://player.vimeo.com/video/*"));

        op.setProvider_name("Vimeo");
        op.setProvider_url("https://vimeo.com/");
        op.setEndpoints(Arrays.asList(ep));

        OembedProviderRes opr = new OembedProviderRes();
        opr.setType("video");
        opr.setVersion("1.0");
        opr.setProvider_name("Vimeo");
        opr.setProvider_url("https://vimeo.com/");
        opr.setTitle("A Time Artifact - work in progress");
        opr.setAuthor_name("Depict_tk");
        opr.setAuthor_url("https://vimeo.com/user5346842");
        opr.setHtml("<iframe src=\\\"https://player.vimeo.com/video/20097015?h=08095da358&amp;app_id=122963\\\" width=\\\"640\\\" height=\\\"480\\\" frameborder=\\\"0\\\" allow=\\\"autoplay; fullscreen; picture-in-picture\\\" allowfullscreen title=\\\"A Time Artifact - work in progress\\\"></iframe>");
        opr.setWidth(640);
        opr.setHeight(480);
        opr.setThumbnail_url("https://i.vimeocdn.com/filter/overlay?src0=https%3A%2F%2Fi.vimeocdn.com%2Fvideo%2F128068226-3e88e664fc0805498112c738d3a44fe6319a6385f63e3a2af526b35440a15ed5-d_640&src1=http%3A%2F%2Ff.vimeocdn.com%2Fp%2Fimages%2Fcrawler_play.png");
        opr.setThumbnail_width(640);
        opr.setHeight(480);
        opr.setUrl("/videos/20097015");

        String requestUrl = "https://vimeo.com/20097015";

        given(apiClient.requestOembedProviders()).willReturn(List.of());

        OembedProviderRes respond = service.requestOembedDataByUrl(requestUrl);

        assertThat(respond.type).isBlank();
        assertThat(respond.provider_name).isBlank();
        assertThat(respond.width).isEqualTo(0);
    }
}