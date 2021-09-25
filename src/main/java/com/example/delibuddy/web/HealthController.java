package com.example.delibuddy.web;

import com.example.delibuddy.web.dto.OkayDto;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Health")
@RestController
public class HealthController {

    @GetMapping("/")
    public OkayDto health() {
        return new OkayDto();
    }
}
