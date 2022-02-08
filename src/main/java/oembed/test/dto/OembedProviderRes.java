package oembed.test.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OembedProviderRes {
    public OembedProviderRes(String msg){
        this.msg = msg;
    }

    public String type ="";
    public String version = "";
    public String title = "";
    public String author_name = "";
    public String author_url = "";
    public String provider_name = "";
    public String provider_url = "";
    public String thumbnail_url = "";
    public String url = "";
    public String html = "";
    public String msg = "";
    public String cache_age = "";
    public int thumbnail_width;
    public int thumbnail_height;
    public int width;
    public int height;
}
