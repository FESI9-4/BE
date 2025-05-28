package com.idol.board.repository.comment;

import com.idol.board.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

}
