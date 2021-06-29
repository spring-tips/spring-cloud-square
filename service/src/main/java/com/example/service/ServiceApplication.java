package com.example.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}

@RestController
class GreetingsRestController {

    private int port;

    @EventListener
    public void webServerInitialized(WebServerInitializedEvent wsie) {
        this.port = wsie.getWebServer().getPort();
    }

    @GetMapping("/hello/{name}")
    Map<String, String> hello(@PathVariable String name) {
        return Map.of("greeting", "Hello, " + name + ", from localhost:" + this.port + "!");
    }

}