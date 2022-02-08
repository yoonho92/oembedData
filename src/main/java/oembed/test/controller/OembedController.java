package oembed.test.controller;

import lombok.RequiredArgsConstructor;
import oembed.test.service.OembedService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class OembedController {
    private final OembedService oembedService;

    @GetMapping(value = "/")
    public String home() {

        return "index";
    }

    @GetMapping(value = "/search")
    public String requestOembedDataByUrl(@RequestParam("url") String url, Model model) {
        model.addAttribute("oembedData", oembedService.requestOembedDataByUrl(url));
        return "index";
    }

}
