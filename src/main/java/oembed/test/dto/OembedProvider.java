package oembed.test.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class OembedProvider {

    public String provider_name;
    public String provider_url;
    public List<EndPoint> endpoints;

    @Data
    public static class EndPoint{
        public List<String> schemes;
        public String url;
        public List<String> formats;
        public String discovery;
    }

}
