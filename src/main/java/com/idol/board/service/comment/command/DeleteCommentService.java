package com.idol.board.service.comment.command;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Comment;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.board.repository.comment.CommentRepository;
import com.idol.board.repository.comment.CommentRepositoryCustom;
import com.idol.board.usecase.comment.command.DeleteCommentUseCase;
import com.idol.global.exception.IllegalArgumentException;
import com.idol.global.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteCommentService implements DeleteCommentUseCase {
    private final CommentRepository commentRepository;

    @Override
    public void delete(Long commentId, Long writerId) {
        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new NotFoundException("Comment", commentId));

        validateUserHasPermission(comment, writerId);
        // 현재 댓글이 부모와 자식 댓글이 존재하는지 확인
        if(hasChildren(comment)){
            validateCommentIsRoot(comment);

        }else{
            comment.markAsDeleted();
        }


//        // Comment 정보 호출
//        commentRepository.findByCommentId(commentId)
//                .ifPresent(comment -> {
//                    // 현재 댓글이 부모와 자식 댓글이 존재하는지 확인
//                    if(hasChildren(comment)){
//                        // 대댓글일때
//                        if(!comment.isRoot()) {
//                            comment.markAsDeleted();
//                        }else{
//                            comment.markAsDeleted();
//                            commentRepository.findByParentCommentId(commentId)
//                                    .ifPresent(
//                                            childComment -> {
//                                                childComment.markAsDeleted();
//                                            }
//                                    );
//                        }
//
//                    }else{      // 혼자 있다면 본인 삭제
//                        comment.markAsDeleted();
//                    }
//                });
    }


    // 자식 댓글이 있는지만 확인하면 되는 것이므로 해당 카운트가 2개까지 카운트 하고 2개이면 대댓글이 있다고 판단 (2개 이상부터는 대댓글이 있는 것이기에 2개까지 구해도 됨)
    private boolean hasChildren(Comment comment) {
        return commentRepository.relatedCommentCountBy(comment.getArticleId(), comment.getParentCommentId(), 2L) == 2;
    }

    private void validateUserHasPermission(Comment comment, Long writerId) {
        if (comment.getWriterId() !=  writerId) {
            throw new IllegalArgumentException("Comment",comment.getCommentId());
        }
    }

    private void validateCommentIsRoot(Comment comment) {
        if(!comment.isRoot()){
            comment.markAsDeleted();
        }else{
            comment.markAsDeleted();
            validateParentCommentIsPresent(comment.getCommentId());
        }
    }

    private void validateParentCommentIsPresent(Long commentId){
        Comment childComment = commentRepository.findByParentCommentId(commentId)
                .orElseThrow(() -> new NotFoundException("Comment", commentId));

        childComment.markAsDeleted();
    }

}
