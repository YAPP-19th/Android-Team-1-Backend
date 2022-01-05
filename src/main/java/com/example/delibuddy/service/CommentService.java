package com.example.delibuddy.service;

import com.example.delibuddy.domain.comment.Comment;
import com.example.delibuddy.domain.comment.CommentRepository;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.party.PartyRepository;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.util.fcm.FcmRequest;
import com.example.delibuddy.util.fcm.FcmService;
import com.example.delibuddy.web.dto.CommentCreationRequestDto;
import com.example.delibuddy.web.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final PartyRepository partyRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsInParty(Long partyId) {
        return commentRepository.findByPartyAndParentIsNull(partyRepository.getById(partyId), Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto create(CommentCreationRequestDto dto, String writerKakaoId) {
        User writer = userRepository.findByKakaoId(writerKakaoId).get();
        Party party = partyRepository.getById(dto.getPartyId());

        Comment comment = dto.toEntity(writer, party);

        if (dto.getParentId() != null) {
            Comment parent = commentRepository.getById(dto.getParentId());
            if (parent.hasParent()) {
                throw new IllegalArgumentException("대댓글에는 대댓글을 달 수 없습니다.");
            }
            comment.setParent(parent);
        }
        commentRepository.save(comment);

        // Party party = partyRepository.getById(dto.getPartyId());
        // party.getComments().add(comment);


        // TODO: test 할 떄 mocking 해서 FcmService.sendToToken 호출되었는지 검증하기
        if (party.getLeader().getFcmToken() != null && !party.getLeader().getFcmToken().isEmpty()) {
            FcmService.sendToToken(
                FcmRequest.builder()
                    .title("새로운 댓글이 달렸습니다.")
                    .body("딜리버디와 함께 새로운 댓글을 확인해 보아요 \uD83C\uDFB5~")
                    .route("delibuddy://comment?partyId=" + party.getId().toString() + "&commentId=" + comment.getId().toString())
                    .build(),
                party.getLeader().getFcmToken()
            );
        }

        return new CommentResponseDto(comment);
    }

    @Transactional
    public void delete(Long id, String writerKakaoId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("comment 가 없습니다."));
        User writer = userRepository.findByKakaoId(writerKakaoId).get();
        if (!writer.getId().equals(comment.getWriter().getId())) {
            throw new IllegalArgumentException("작성자만 댓글을 수정할 수 있습니다.");
        }

        if(comment.getChildren().size() != 0) {
            comment.setAsIsDeleted();
        } else {
            commentRepository.delete(comment);
        }
    }


}
