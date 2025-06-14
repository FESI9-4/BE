package com.idol.board.repository.comment;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    @Query("select a from Comment a where a.isDeleted = false and a.articleId = :articleId")
    List<Comment> findByArticleId(Long articleId);
}
