package com.example.delibuddy.web.auth;

import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.service.MyUserDetailsService;
import com.example.delibuddy.util.JwtUtil;
import com.example.delibuddy.util.KakaoMyInfo;
import com.example.delibuddy.util.RandomProfileImage;
import com.example.delibuddy.web.dto.AuthenticationRequestDto;
import com.example.delibuddy.web.dto.AuthenticationResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import static com.example.delibuddy.util.KakaoRestHelper.getKakaoMyInfo;

@Api(tags = "Auth")
@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RandomProfileImage randomProfileImage;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST) // PostMapping 으로 바꾸기
    public AuthenticationResponseDto createAuthenticationToken(@RequestBody AuthenticationRequestDto authenticationRequestDto) throws Exception {
        KakaoMyInfo kakaoMyinfo;

        try {
            kakaoMyinfo = getKakaoMyInfo(authenticationRequestDto.getToken());
        } catch (JsonProcessingException | HttpClientErrorException e) {
            throw new Exception("Invalid kakao access token", e);
        }

        User user = userRepository.findByKakaoId(kakaoMyinfo.getKakaoId()).orElse(kakaoMyinfo.toUser(randomProfileImage.getRandomProfileImage()));
        userRepository.save(user);

        final String jwt = jwtUtil.generateToken(user.getKakaoId());

        return new AuthenticationResponseDto(jwt, user.getId());
    }
}
