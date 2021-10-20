package com.example.delibuddy.service.comment;

import com.example.delibuddy.domain.comment.CommentRepository;
import com.example.delibuddy.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CommentServiceTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Test
    void 댓글을_등록할_수_있다(){
    }

    @Test
    void 대댓글의_대댓글을_등록할_수_없다(){
    }

    @Test
    void 댓글을_조회하면_자식들도_조회된다(){}

    @Test
    void 자식_댓글이_없는_경우에는_댓글이_삭제된다(){}

    @Test
    void 자식_댓글이_있는_경우에는_댓글_내용이_사라진다(){}

}
