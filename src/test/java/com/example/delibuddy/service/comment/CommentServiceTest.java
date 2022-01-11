package com.example.delibuddy.service.comment;

import com.example.delibuddy.domain.category.Category;
import com.example.delibuddy.domain.category.CategoryRepository;
import com.example.delibuddy.domain.comment.Comment;
import com.example.delibuddy.domain.comment.CommentRepository;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.party.PartyFactory;
import com.example.delibuddy.domain.party.PartyRepository;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.domain.user.UserRepository;
import com.example.delibuddy.service.CommentService;
import com.example.delibuddy.web.dto.CommentCreationRequestDto;
import com.example.delibuddy.web.dto.CommentResponseDto;
import com.example.delibuddy.web.dto.PartyCreationRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class CommentServiceTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PartyFactory partyFactory;

    @Test
    void 댓글을_등록할_수_있다(){
        //given : 파티와 유저와 댓글 달 내용
        Category category = categoryRepository.save(new Category("hihi", "hi", "google.com", "FFFFFF"));
        User writer = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );
        Party party = partyRepository.save(
                partyFactory.createParty(
                        new PartyCreationRequestDto("test", "test", "", "", "", "POINT (1 1)", category.getId(), 5, LocalDateTime.now()),
                        writer
                )
        );

        String body = "테스트 댓글";
        CommentCreationRequestDto dto = CommentCreationRequestDto.builder()
                .body(body)
                .partyId(party.getId())
                .build();

        //when : 댓글을 생성한다.
        CommentResponseDto response = commentService.create(dto, writer.getKakaoId());


        //then : 댓글이 뾰로롱~ 생성이 된다.
        Comment comment = commentRepository.getById(response.getId());
        assertThat(comment.getBody()).isEqualTo(body);
        assertThat(comment.getParty()).isEqualTo(party);
        assertThat(comment.getWriter()).isEqualTo(writer);
    }

    @Test
    void 대댓글을_등록할_수_있다(){
        //given : 파티, 유저, 부모 댓글과 댓글 달 내용
        Category category = categoryRepository.save(new Category("hihi", "hi", "google.com", "FFFFFF"));
        User writer = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );
        Party party = partyRepository.save(
                partyFactory.createParty(
                        new PartyCreationRequestDto("test", "test", "", "", "", "POINT (1 1)", category.getId(), 5, LocalDateTime.now()),
                        writer
                )
        );
        Comment parent = commentRepository.save(Comment.builder()
                        .body("테스트 부모 댓글")
                        .party(party)
                        .writer(writer)
                        .build());

        String body = "테스트 대댓글";
        CommentCreationRequestDto dto = CommentCreationRequestDto.builder()
                .body(body)
                .partyId(party.getId())
                .parentId(parent.getId())
                .build();

        //when : 대댓글을 생성한다.
        CommentResponseDto response = commentService.create(dto, writer.getKakaoId());


        //then : 대댓글이 뾰로롱~ 생성이 된다.
        Comment comment = commentRepository.getById(response.getId());
        assertThat(comment.getBody()).isEqualTo(body);
        assertThat(comment.getParty()).isEqualTo(party);
        assertThat(comment.getParent()).isEqualTo(parent);
        assertThat(comment.getWriter()).isEqualTo(writer);
        assertThat(comment).isIn(parent.getChildren());
    }

    @Test
    void 대댓글의_대댓글을_등록할_수_없다(){
        //given : 파티, 유저, 부모 댓글과 자식 댓글, 댓글 달 내용
        Party party = partyRepository.save(Party.builder().build());
        User writer = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );
        Comment parent = commentRepository.save(Comment.builder()
                        .body("테스트 부모 댓글")
                        .party(party)
                        .writer(writer)
                        .build());

        Comment child = commentRepository.save(Comment.builder()
                        .body("테스트 자식댓글")
                        .party(party)
                        .parent(parent)
                        .writer(writer)
                        .build());

        String body = "테스트 자식댓글의 대댓글";
        CommentCreationRequestDto dto = CommentCreationRequestDto.builder()
                .body(body)
                .partyId(party.getId())
                .parentId(child.getId())
                .build();

        //expect : 자식댓글의 대댓글을 등록한다. 예외터짐
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> commentService.create(dto, writer.getKakaoId()));
    }

    @Test
    void 댓글을_조회하면_자식들도_조회된다(){}

    @Test
    void 자식_댓글이_없는_경우에는_댓글이_삭제된다(){
        //given : 파티와 유저와 댓글
        Party party = partyRepository.save(Party.builder().build());
        User writer = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );

        Comment comment = commentRepository.save(Comment.builder()
                .body("테스트 댓글")
                .party(party)
                .writer(writer)
                .build());

        //when : 댓글을 삭제한다.
        commentService.delete(comment.getId(), writer.getKakaoId());


        //then : 댓글이 뾰로롱~ 삭제 된다. 댓글을 불러오려고 하면 예외 터짐
        assertThrows(JpaObjectRetrievalFailureException.class, () -> commentRepository.getById(comment.getId()));


    }

    @Test
    void 자식_댓글이_있는_경우에는_댓글_내용이_사라진다(){
        //given : 파티,유저, 부모 댓글과 자식 댓글
        Party party = partyRepository.save(Party.builder().build());
        User writer = userRepository.save(
                User.builder()
                        .nickName("test")
                        .kakaoId("test-kakao-id")
                        .build()
        );
        Comment parent = commentRepository.save(Comment.builder()
                .body("테스트 부모 댓글")
                .party(party)
                .writer(writer)
                .build());

        Comment child = commentRepository.save(Comment.builder()
                .body("테스트 자식댓글")
                .party(party)
                .writer(writer)
                .build());

        child.setParent(parent);

        //when : 부모 댓글을 삭제한다.
        commentService.delete(parent.getId(), writer.getKakaoId());

        //then : 댓글 삭제는 안되고, isDeleted 만 참으로 설정됨
        Comment comment = commentRepository.getById(parent.getId());
        assertTrue(comment.getIsDeleted());
    }

}
