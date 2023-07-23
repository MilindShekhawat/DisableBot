package com.miilind.disablebot.controllor;

import com.miilind.disablebot.dto.OpenAiRequest;
import com.miilind.disablebot.dto.OpenAiResponse;
import com.miilind.disablebot.listeners.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/bot")
public class DisableBotControllor extends MessageListener {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    String openaiApiKey;
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping("/chat")
    public String chat(String prompt){
        OpenAiRequest request = new OpenAiRequest(model, prompt);
        OpenAiResponse openAiResponse = restTemplate.postForObject(apiUrl, request, OpenAiResponse.class);
        return openAiResponse
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();
    }
}
