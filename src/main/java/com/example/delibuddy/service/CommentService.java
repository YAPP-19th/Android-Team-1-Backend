package com.example.delibuddy.service;

import com.example.delibuddy.domain.comment.Comment;
import com.example.delibuddy.domain.comment.CommentRepository;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.party.PartyRepository;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.web.dto.CommentCreationRequestDto;
import com.example.delibuddy.web.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final PartyRepository partyRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

//    @Transactional(readOnly = true)
//    public List<CommentResponseDto> getCommentsInParty(Long partyId) {
//        // TODO 댓글은 작성 시간순 정렬, 댓글 아래에 대댓글이 보이게 출력
//        // TODO 삭제됐지만 자식 댓글이 있는 경우 "삭제된 댓글입니다"로 출력
//    }


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
            parent.getChildren().add(comment);
            comment.setParent(parent);
        }
        commentRepository.save(comment);
        // Party party = partyRepository.getById(dto.getPartyId());
        // party.getComments().add(comment);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("comment 가 없습니다."));
        if(comment.getChildren().size() != 0) {
            comment.setAsIsDeleted();
        } else {
            commentRepository.delete(comment);
        }
    }


}
