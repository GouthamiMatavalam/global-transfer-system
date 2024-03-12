package com.assignment.globaltransfersystem.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationConfigTest {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void restTemplateShouldNotBeNull() {
        assertNotNull(restTemplate);
    }
}