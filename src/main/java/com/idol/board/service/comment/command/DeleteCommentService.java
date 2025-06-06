package com.idol.board.service.comment.command;

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
public class DeleteCommentService implements DeleteCommentUseCase {
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void delete(Long commentId){
        // Comment 정보 호출
        commentRepository.findById(commentId)
                // 존재한다면 해당 comment 객체를 가지고 진행
                .ifPresent(comment -> {
                    // 만약 대댓글이나 부모댓글이 존재하는지 확인한 후
                    if(hasChildren(comment)){
                        // 대댓글이나 부모댓글이 존재한다면 현재 댓글의 삭제 여부를 true로 바꿔줌
                        comment.markAsDeleted();
                    // 대댓글이나 부모댓글이 없다면 완전 삭제
                    }else{
                        comment.markAsDeleted();        // delete(comment)  -> 부모댓글, 자식댓글 모두 삭제되었을때 db에서 모든 데이터 삭제하는 방식
                    }
                });
    }


    // 자식 댓글이 있는지만 확인하면 되는 것이므로 해당 카운트가 2개까지 카운트 하고 2개이면 대댓글이 있다고 판단 (2개 이상부터는 대댓글이 있는 것이기에 2개까지 구해도 됨)
    private boolean hasChildren(Comment comment) {
        return commentRepository.countBy(comment.getArticleId(), comment.getParentCommentId(), 2L) == 2;
    }

    // 재귀적으로 부모객체가 삭제되어 있는 상태에서 대댓글을 삭제한다면 부모댓글과 대댓글을 모두 완전 삭제 시킴
    private void delete(Comment comment) {
        // 현재 Comment를 완전 삭제함
        commentRepository.delete(comment);

        // 만약 comment가 부모 댓글이 아니라 대댓글이라면
        if (!comment.isRoot()) {
            // 대댓글의 부모 댓글 Id값을 통해 부모 Comment 객체를 가져와
            commentRepository.findById(comment.getParentCommentId())
                    // 해당 객체의 대댓글이 없다면
                    .filter(not(this::hasChildren))
                    // 해당 객체 완전 삭제함
                    .ifPresent(this::delete);
        // 자식 댓글이 삭제되어 있는 상태에서 부모 댓글을 삭제하게 된다면
        }else{
            commentRepository.deleteAllChildComments(comment.getParentCommentId());
        }
    }
}
