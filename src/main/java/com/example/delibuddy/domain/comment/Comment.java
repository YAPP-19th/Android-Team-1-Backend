package com.example.delibuddy.domain.comment;

import com.example.delibuddy.domain.BaseTimeEntity;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private List<Comment> children = new ArrayList<>();

    @Column(columnDefinition = "VARCHAR(1000)", nullable=false)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "party_id")
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User writer;

    private Boolean isDeleted;

    public void setAsIsDeleted(){
        isDeleted = Boolean.TRUE;
    }
    public Boolean hasParent(){
        return parent != null;
    }
    public void setParent(Comment parent){
        parent.getChildren().add(this);
        this.parent = parent;
    }

}
