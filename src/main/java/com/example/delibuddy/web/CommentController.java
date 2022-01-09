package com.example.delibuddy.web;

import com.example.delibuddy.service.CommentService;
import com.example.delibuddy.service.FcmService;
import com.example.delibuddy.web.auth.MyUserDetails;
import com.example.delibuddy.web.dto.CommentCreationRequestDto;
import com.example.delibuddy.web.dto.CommentResponseDto;
import com.example.delibuddy.web.dto.OkayDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Comment")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;
    private final FcmService fcmService;

    @PostMapping("${api.v1}/comments")
    public CommentResponseDto createComment(@RequestBody CommentCreationRequestDto requestDto) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CommentResponseDto commentResponseDto = commentService.create(requestDto, userDetails.getUsername());
        fcmService.newComment(requestDto.getPartyId(), commentResponseDto.getId());
        return commentResponseDto;
    }

    @GetMapping("${api.v1}/parties/{id}/comments")
    public List<CommentResponseDto> getCommentsInParty(@PathVariable Long id) {
        return commentService.getCommentsInParty(id);
    }

    @DeleteMapping("${api.v1}/comments/{id}")
    public OkayDto deleteComment(@PathVariable Long id) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentService.delete(id, userDetails.getUsername());
        return new OkayDto();
    }

}
