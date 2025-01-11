package org.apache.rocketmq.samples.springboot.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test UseCase.
 */
@RestController
@RequestMapping("/")
public class RestDemoController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/{path}")
    public ResponseEntity<?> path(@PathVariable("path") String path) {
        int resp = 401;
        logger.debug(">>request url: {}", path);
        int value = Integer.parseInt(path);
        if (value / 10 == 0) {
            logger.warn("path{} is 10 times.", path);
        }
        if (value % 2 == 0) {
            resp = 200;
        } else if (value % 3 == 0) {
            resp = 302;
        } else if (value % 5 == 0) {
            resp = 500;
        }
        logger.debug("<<request url: {},resp: {} ", path, resp);
        return ResponseEntity.status(resp).body(path);
    }


}
