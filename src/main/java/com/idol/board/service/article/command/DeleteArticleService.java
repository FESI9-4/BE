package com.idol.board.service.article.command;

import com.idol.board.domain.entity.Article;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.board.repository.comment.CommentRepository;
import com.idol.board.usecase.article.command.DeleteArticleUseCase;
import com.idol.global.exception.ArticleNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteArticleService implements DeleteArticleUseCase {

    private final ArticleRepository articleJpaRepository;
    private final CommentRepository commentJpaRepository;

    @Override
    @Transactional
    public Long delete(Long articleId){
        Article article = articleJpaRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));

        // 1. 게시글과 연관된 댓글들을 모두 로드 후 삭제처리
        commentJpaRepository.softDeleteAllByArticleId(articleId);

        // 2. 게시글 삭제 처리
        article.markAsDeleted();

        return articleId;
    }
}
