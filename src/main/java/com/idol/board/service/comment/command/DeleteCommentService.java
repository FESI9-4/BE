package com.idol.board.service.comment.command;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Comment;
import com.idol.board.repository.comment.CommentRepository;
import com.idol.board.repository.comment.CommentRepositoryCustom;
import com.idol.board.usecase.comment.command.DeleteCommentUseCase;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteCommentService implements DeleteCommentUseCase {
    private final CommentRepository commentRepository;

    @Override
    public void delete(Long commentId){
        // Comment 정보 호출
        commentRepository.findByCommentId(commentId)
                .ifPresent(comment -> {
                    // 만약 대댓글이나 부모댓글이 존재하는지 확인한 후
                    if(hasChildren(comment)){
                        // 대댓글일때
                        if(!comment.isRoot()) {
                            comment.markAsDeleted();
                        }else{
                            comment.markAsDeleted();
                            commentRepository.findByParentCommentId(commentId)
                                    .ifPresent(
                                            childComment -> {
                                                childComment.markAsDeleted();
                                            }
                                    );
                        }

                    }else{
                        comment.markAsDeleted();        // delete(comment)  -> 부모댓글, 자식댓글 모두 삭제되었을때 db에서 모든 데이터 삭제하는 방식
                    }
                });
    }


    // 자식 댓글이 있는지만 확인하면 되는 것이므로 해당 카운트가 2개까지 카운트 하고 2개이면 대댓글이 있다고 판단 (2개 이상부터는 대댓글이 있는 것이기에 2개까지 구해도 됨)
    private boolean hasChildren(Comment comment) {
        return commentRepository.relatedCommentcountBy(comment.getArticleId(), comment.getParentCommentId(), 2L) == 2;
    }

}
