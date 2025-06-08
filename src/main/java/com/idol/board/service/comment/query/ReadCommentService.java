package com.idol.board.service.comment.query;

import com.idol.board.dto.response.comment.CommentResponseDto;
import com.idol.board.repository.comment.CommentRepository;
import com.idol.board.repository.mapper.CommentReadQueryResult;
import com.idol.board.usecase.comment.query.ReadCommentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadCommentService implements ReadCommentUseCase {
    private final CommentRepository commentJpaRepository;

    @Transactional(readOnly = true)
    public List<CommentResponseDto> readAll(Long articleId, Long lastParentCommentId, Long lastCommentId, Long limit){
        List<CommentReadQueryResult> comments = lastParentCommentId == null || lastCommentId == null ?
                commentJpaRepository.findAllInfiniteScroll(articleId, limit) :
                commentJpaRepository.findAllInfiniteScroll(articleId,lastParentCommentId,lastCommentId,limit);

        return comments.stream()
                .map(CommentResponseDto::from)
                .toList();
    }
}
