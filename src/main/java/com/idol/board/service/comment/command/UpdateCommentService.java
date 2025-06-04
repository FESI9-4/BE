package com.idol.board.service.comment.command;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Comment;
import com.idol.board.dto.request.comment.CommentCreateRequestDto;
import com.idol.board.dto.request.comment.CommentUpdateRequestDto;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.board.repository.comment.CommentRepository;
import com.idol.board.usecase.comment.command.CreateCommentUseCase;
import com.idol.board.usecase.comment.command.UpdateCommentUseCase;
import com.idol.global.common.snowflake.Snowflake;
import com.idol.global.exception.ArticleNotFoundException;
import com.idol.global.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateCommentService implements UpdateCommentUseCase {

    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Long updateComment(CommentUpdateRequestDto commentUpdateRequestDto) {
        Comment comment = commentRepository.findById(commentUpdateRequestDto.commentId())
                .orElseThrow(() -> new CommentNotFoundException(commentUpdateRequestDto.commentId()));

        comment.update(commentUpdateRequestDto);

        return comment.getCommentId();
    }

}
