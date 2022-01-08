package com.example.delibuddy.domain.comment;

import com.example.delibuddy.domain.BaseTimeEntity;
import com.example.delibuddy.domain.party.Party;
import com.example.delibuddy.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Comment> children = new ArrayList<Comment>();

    @Column(columnDefinition = "VARCHAR(1000)", nullable=false)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    @OnDelete(action = OnDeleteAction.CASCADE)  // https://stackoverflow.com/questions/14875793/jpa-hibernate-how-to-define-a-constraint-having-on-delete-cascade
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    private Boolean isDeleted = Boolean.FALSE;

    @Builder
    public Comment(Comment parent, String body, Party party, User writer){
        this.parent = parent;
        this.body = body;
        this.party = party;
        this.writer = writer;
    }

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
