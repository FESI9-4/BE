package com.idol.board.service.comment.command;

import com.idol.board.domain.entity.Comment;
import com.idol.board.dto.request.comment.CommentUpdateRequestDto;
import com.idol.board.repository.comment.CommentRepository;
import com.idol.board.usecase.comment.command.UpdateCommentUseCase;
import com.idol.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateCommentService implements UpdateCommentUseCase {

    private final CommentRepository commentRepository;

    @Override
    public Long updateComment(CommentUpdateRequestDto commentUpdateRequestDto) {
        Comment comment = commentRepository.findByCommentId(commentUpdateRequestDto.commentId())
                .orElseThrow(() -> new NotFoundException("Comment", commentUpdateRequestDto.commentId()));

        comment.update(commentUpdateRequestDto);

        return comment.getCommentId();
    }

}
