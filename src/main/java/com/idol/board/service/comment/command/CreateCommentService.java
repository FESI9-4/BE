package com.idol.board.service.comment.command;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Comment;
import com.idol.board.dto.request.comment.CommentCreateRequestDto;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.board.repository.comment.CommentRepository;
import com.idol.board.usecase.comment.command.CreateCommentUseCase;
import com.idol.global.common.snowflake.Snowflake;
import com.idol.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateCommentService implements CreateCommentUseCase {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Override
    public Long createComment(CommentCreateRequestDto requestDto, Long writerId, Long articleId) {
        Article article = articleRepository.findByArticleId(articleId)
                .orElseThrow(() -> new NotFoundException("Article", articleId));

        // Comment(부모 객체) 값이 반환되었다면 대댓글, NUll 반환되었다면 부모 객체
        Comment parent = findParent(requestDto);

        Comment comment = commentRepository.save(
                requestDto.toEntity(
                        article.getArticleId(),
                        writerId,
                        parent == null ? null : parent.getCommentId()
                ));

        if(parent == null){
            comment.addCommentParentId();
//            comment.updateParentCheck();
        }

        return comment.getCommentId();
    }


    // 부모 댓글이 있는지 없는지 확인
    private Comment findParent(CommentCreateRequestDto requestDto){
        Long parentCommentId = requestDto.parentCommentId();
        // 부모 댓글이 null이면 null 반환 즉, parentCommentId값이 없다는 것은 새로운 comment이므로 부모 댓글임
        if(parentCommentId == null){
            return null;
        }

        // 부모 댓글이 존재한다면 해당 request는 대댓글이므로 해당 부모 객체의 값을 불러옴
        return commentRepository.findById(parentCommentId)
                .filter(Comment::isRoot)                   // 해당 객체가 부모 객체인지 확인
                .orElseThrow();
    }
}
