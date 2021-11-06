package com.example.delibuddy;

import com.example.delibuddy.domain.user.QUser;
import com.example.delibuddy.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.URI_TYPE;

@SpringBootTest
@Transactional
class DelibuddyApplicationTests {

    @Autowired
    EntityManager em;

    @Test
    void contextLoads() {
        User user = User.builder().kakaoId("tesk-kakao-id").nickName("test").build();
        em.persist(user);

        JPAQueryFactory query = new JPAQueryFactory(em);
        QUser qUser = QUser.user;

        User result = query.selectFrom(qUser).where(qUser.id.eq(user.getId())).fetchOne();

        assertThat(result).isEqualTo(user);
        assertThat(result.getId()).isEqualTo(user.getId());
    }

}
