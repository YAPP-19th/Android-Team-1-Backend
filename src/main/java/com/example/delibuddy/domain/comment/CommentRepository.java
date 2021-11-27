package com.example.delibuddy.domain.comment;

import com.example.delibuddy.domain.party.Party;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query
    List<Comment> findByPartyAndParentIsNull(Party party, Sort sort);
}
