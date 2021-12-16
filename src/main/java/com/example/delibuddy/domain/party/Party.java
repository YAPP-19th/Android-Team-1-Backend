package com.example.delibuddy.domain.party;

import com.example.delibuddy.domain.BaseTimeEntity;
import com.example.delibuddy.domain.category.Category;
import com.example.delibuddy.domain.user.User;
import com.example.delibuddy.web.dto.PartyEditRequestDto;
import com.example.delibuddy.web.dto.PartyResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
public class Party extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private User leader;

    @OnDelete(action = OnDeleteAction.CASCADE)  // https://stackoverflow.com/questions/14875793/jpa-hibernate-how-to-define-a-constraint-having-on-delete-cascade
    @OneToMany(mappedBy = "party", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PartyUser> users = new ArrayList<>();

    @Column
    private Integer targetUserCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(columnDefinition = "CHAR(255)")
    private String title;

    @Column(columnDefinition = "VARCHAR(1000)")
    private String body;

    @Column
    private Point coordinate;

    @Column
    private String placeName;

    @Column
    private String placeNameDetail;

    @Column
    private String openKakaoUrl;

    @Column
    private PartyStatus status;

    @Column
    private LocalDateTime orderTime;

    @Builder
    public Party(User leader, Point coordinate, String title, String body, String placeName, String placeNameDetail,
    String openKakaoUrl, LocalDateTime orderTime, Integer targetUserCount, Category category) {
        this.leader = leader;
        this.coordinate = coordinate;
        this.title = title;
        this.body = body;
        this.placeName = placeName;
        this.placeNameDetail = placeNameDetail;
        this.openKakaoUrl = openKakaoUrl;
        this.orderTime = orderTime;
        this.status = PartyStatus.OPEN;
        this.targetUserCount = targetUserCount;
        this.category = category;
    }

    public boolean isIn(User user) {
        return users.stream().map(PartyUser::getUser).anyMatch(u -> u.getId().equals(user.getId()));
    }

    public void join(PartyUser partyUser) {
        // TODO: 아니 이걸 해줘야되?? PartyUser 를 생성해서 flush 하면 자동으로 감지될 수는 없나?
        users.add(partyUser);
        partyUser.getUser().getParties().add(partyUser);
    }
    public void leave(PartyUser partyUser) {
        users.remove(partyUser);
        partyUser.getUser().getParties().remove(partyUser);
    }

    public void edit(PartyEditRequestDto dto) {
        title = dto.getTitle() != null ? dto.getTitle() : title;
        body = dto.getBody() != null ? dto.getBody() : body;
        coordinate = dto.getCoordinate() != null ? dto.getPoint() : coordinate;
    }

    public void setStatus(PartyStatus status) {
        this.status = status;
    }
}
