package com.idol.board.service.comment.query;

import com.idol.board.domain.entity.Comment;
import com.idol.board.dto.response.comment.CommentResponseDto;
import com.idol.board.repository.comment.CommentRepository;
import com.idol.board.repository.mapper.CommentReadDao;
import com.idol.board.usecase.comment.query.ReadCommentUseCase;
import com.idol.global.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadCommentService implements ReadCommentUseCase {
    private final CommentRepository commentJpaRepository;

    public List<CommentResponseDto> readAll(Long articleId, Long lastParentCommentId, Long lastCommentId, Long limit){
        List<CommentReadDao> comments = lastParentCommentId == null || lastCommentId == null ?
                commentJpaRepository.findAllInfiniteScroll(articleId, limit) :
                commentJpaRepository.findAllInfiniteScroll(articleId,lastParentCommentId,lastCommentId,limit);

        return comments.stream()
                .map(CommentResponseDto::from)
                .toList();
    }
}
