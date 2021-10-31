package com.example.delibuddy.domain.comment;

import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.user.User;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.List;

@SpringBootTest
@Transactional
public class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

//    @After
//    public void cleanup(){
//        commentRepository.deleteAll();
//    }

    @Test
    public void 댓글_등록() throws ParseException {
        // Given
        String body = "테스트 댓글";
        Party party = Party.builder().title("테스트 파티 제목")
                .body("테스트 파티 내용").build();
        User writer = User.builder().kakaoId("테스트 카카오 아이디").build();

        commentRepository.save(Comment.builder()
                .body(body)
                .party(party)
                .writer(writer)
                .build());
        // When
        List<Comment> commentList = commentRepository.findAll();

        // Then
        Comment comment = commentList.get(0);
        assertEquals(comment.getBody(), body);
        assertEquals(comment.getParty(), party);
        assertEquals(comment.getWriter(), writer);


;    }

}
