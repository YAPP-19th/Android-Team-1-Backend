package com.example.delibuddy.web;

import com.example.delibuddy.service.UserService;
import com.example.delibuddy.web.auth.MyUserDetails;
import com.example.delibuddy.web.dto.UserResponseDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "User")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("${api.v1}/users/me")
    public UserResponseDto getMyInfo() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.getMyInfo(userDetails.getUsername());
    }
}
