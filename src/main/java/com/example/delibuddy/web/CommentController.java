package com.example.delibuddy.web;

import com.example.delibuddy.service.CommentService;
import com.example.delibuddy.web.auth.MyUserDetails;
import com.example.delibuddy.web.dto.CommentCreationRequestDto;
import com.example.delibuddy.web.dto.CommentResponseDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Comment")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("api/v1/comments")
    // api/v1/party/{partyId}/comments 가 맞을까..?
    public CommentResponseDto createComment(@RequestBody CommentCreationRequestDto requestDto) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return commentService.create(requestDto, userDetails.getUsername());
    }

}
