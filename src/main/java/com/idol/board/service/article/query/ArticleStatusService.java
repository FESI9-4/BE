package com.idol.board.service.article.query;

import com.idol.board.domain.OpenStatus;
import com.idol.board.domain.entity.Article;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.global.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ArticleStatusService {
    private final ArticleRepository articleRepository;
    private final EntityManager entityManager;

    public void validateCheckOpenStatus(Article article) {
        if(article.getCurrentPerson() >= article.getMinPerson()){
            if(article.getOpenStatus().equals(OpenStatus.PENDING_STATUS)) {
                article.updateOpenStatus(OpenStatus.CONFIRMED_STATUS);
            }
        }
    }

    public OpenStatus validateCheckDeadlineStatus(Long articleId, OpenStatus status, Date deadline) {
        Article article = articleRepository.findByArticleId(articleId)
                .orElseThrow(() -> new NotFoundException("Article", articleId));

        Timestamp deadlineTime = new Timestamp(deadline.getTime());
        if(deadlineTime.before(new Timestamp(System.currentTimeMillis()))){
            if(status.equals(OpenStatus.CONFIRMED_STATUS)) {
                article.updateOpenStatus(OpenStatus.DEADLINE_STATUS);
                return OpenStatus.DEADLINE_STATUS;
            }else if(status.equals(OpenStatus.PENDING_STATUS)){
                article.updateOpenStatus(OpenStatus.CANCELED_STATUS);
                return OpenStatus.CANCELED_STATUS;
            }
        }
        return status;
    }
}
