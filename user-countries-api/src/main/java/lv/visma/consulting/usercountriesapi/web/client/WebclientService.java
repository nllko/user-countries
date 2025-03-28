package lv.visma.consulting.usercountriesapi.web.client;

import lv.visma.consulting.usercountriesapi.web.client.dto.WebClientCountryDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class WebclientService extends BaseWebclient {
    public WebclientService(WebClient webClient) {
        super(webClient);
    }

    public WebClientCountryDto get(String code) {
        final String uri = "alpha/" + code;
        return super.get(uri, WebClientCountryDto.class).getBody();
    }

    public List<WebClientCountryDto> getList(String codes) {
        final String uri = "alpha?codes=" + codes;
        return super.getList(uri, new ParameterizedTypeReference<List<WebClientCountryDto>>() {
        }).getBody();
    }
}
