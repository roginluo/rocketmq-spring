package org.apache.rocketmq.samples.springboot.demo;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class RequestInvoke {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final RestTemplate restTemplate = new RestTemplate();

    ScheduledExecutorService service = Executors.newScheduledThreadPool(20);

    String url = "http://localhost:8282/{path}";

    @PostConstruct
    public void schedule() {
        for (int i = 0; i < 20; i++) {
            service.scheduleAtFixedRate(() -> {
                try {
                    Random random = new Random();
                    Map<String, Object> path = ImmutableMap.of("path", random.nextInt(10));
                    ResponseEntity<String> exchange =
                            restTemplate.getForEntity(url, String.class, path);
                    if (exchange.getStatusCode().is2xxSuccessful()) {
                        logger.info(" request succeed. {} ", exchange.getBody());
                    } else if (exchange.getStatusCode().is3xxRedirection()) {
                        logger.debug(" request redirect. {} ", exchange.getBody());
                    } else if (exchange.getStatusCode().is4xxClientError()) {
                        logger.warn(" request client error. {} ", exchange.getBody());
                    } else if (exchange.getStatusCode().is5xxServerError()) {
                        logger.error(" request server error. {} ", exchange.getBody());
                    }
                } catch (Exception e) {
                    logger.error("exec http request failed.", e);
                }

            }, 10, 3, TimeUnit.SECONDS);
        }
    }

}
