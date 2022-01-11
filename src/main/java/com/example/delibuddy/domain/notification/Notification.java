package com.example.delibuddy.domain.notification;

import com.example.delibuddy.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "CHAR(255)")
    private String title;

    @Column(columnDefinition = "CHAR(255)")
    private String body;

    @Column(columnDefinition = "CHAR(255)")
    private String route;

    @Column
    private Long commentId;  // 물리키 사용하지 않습니다~

    @Column
    private Long partyId;

    @Builder
    public Notification(User user, String title, String body, String route, Long commentId, Long partyId) {
        this.user = user;
        this.title = title;
        this.body = body;
        this.route = route;
        this.commentId = commentId;
        this.partyId = partyId;
    }
}
