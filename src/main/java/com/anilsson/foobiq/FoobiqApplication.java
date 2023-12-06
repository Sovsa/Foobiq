package com.anilsson.foobiq;

import com.anilsson.foobiq.restclient.RestClient;
import com.anilsson.foobiq.service.Line;
import com.anilsson.foobiq.service.TrafikLabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(RestClientConfiguration.class)
public class FoobiqApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoobiqApplication.class, args);
    }

}
