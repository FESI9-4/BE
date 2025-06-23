package com.idol.board.service.article.query;

import com.idol.board.domain.BigCategory;
import com.idol.board.domain.OpenStatus;
import com.idol.board.domain.SmallCategory;
import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Location;
import com.idol.board.domain.entity.Participant;
import com.idol.board.dto.response.article.ArticleListImgResponseDto;
import com.idol.board.dto.response.article.ArticleListResponseDto;
import com.idol.board.dto.response.article.ArticleReadResponseDto;
import com.idol.board.dto.response.participant.ParticipantResponseDto;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.board.repository.fanFal.ParticipantRepository;
import com.idol.board.repository.location.LocationRepository;
import com.idol.board.repository.mapper.ArticleListReadQueryResult;
import com.idol.board.usecase.article.query.ReadArticleUseCase;
import com.idol.domains.member.domain.Member;
import com.idol.domains.member.repository.MemberJpaRepository;
import com.idol.domains.wish.repository.WishRepository;
import com.idol.global.exception.NotFoundException;
import com.idol.imageUpload.dto.GetS3UrlDto;
import com.idol.imageUpload.useCase.ImageGetUserCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional()
public class ReadArticleService implements ReadArticleUseCase {

    private final ArticleRepository articleRepository;
    private final LocationRepository locationRepository;
    private final ImageGetUserCase imageGetUserCase;
    private final MemberJpaRepository memberJpaRepository;
    private final ParticipantRepository participantRepository;
    private final WishRepository wishRepository;

    @Override
    @Transactional(readOnly = true)
    public ArticleReadResponseDto readArticle(Long articleId, Long userId) {
        Article article = articleRepository.findByArticleId(articleId)
                .orElseThrow(() -> new NotFoundException("Article", articleId));

        Member member = memberJpaRepository.findById(article.getWriterId())
                .orElseThrow(() -> new NotFoundException("Member", article.getWriterId()));

        List<Participant> participant = participantRepository.findParticipantFromArticle(articleId);

        List<ParticipantResponseDto> participants = new ArrayList<>();

        if(!participant.isEmpty()){
            for(Participant p : participant){
                String participantImageUrl = "";
                if(!p.getImageKey().equals("")){
                    participantImageUrl = getS3Url(p.getImageKey()).preSignedUrl();
                }
                ParticipantResponseDto pa = new ParticipantResponseDto(participantImageUrl, p.getNickname());
                    participants.add(pa);
            }
        }

        /* TODO::
           2. 현재 게시물 찜되어 있는지 확인
         */
        String articleImageUrl = "";
        if(!article.getArticleImageKey().equals("")){
            articleImageUrl = getS3Url(article.getArticleImageKey()).preSignedUrl();
        }

        String writerImageUrl = "";
        if(!member.getProfileImgUrl().equals("")){
            writerImageUrl = getS3Url(member.getProfileImgUrl()).preSignedUrl();
        }

        Location location = validateLocation(article.getLocationId());


        validateCheckOpenStatus(article);

        boolean wish = wishCheck(articleId,userId);

        ArticleReadResponseDto dto = ArticleReadResponseDto.from(article,location,participants,wish,articleImageUrl,member.getNickname(),writerImageUrl);

        return dto;
    }


    @Override
    @Transactional(readOnly = true)
    public List<ArticleListImgResponseDto> searchArticleList(
            BigCategory bigCategory, SmallCategory smallCategory, String location,
            Long date, String sort, boolean sortAsc, Long limit, Long page, Long memberId) {

        Timestamp dateTime = null;
        if (date != null) {
            dateTime =  new Timestamp(date * 1000);
        }

        List<ArticleListImgResponseDto> searchArticles = articleRepository.findArticleList(
                bigCategory, smallCategory, location, dateTime, sort,  sortAsc, limit,  (page -1) * limit).stream()
                .map(result ->
                        ArticleListImgResponseDto.from(
                                result,
                                validateLocation(result.locationId()).getRoadNameAddress(),
                                validateCheckDeadlineStatus(result.articleId(), result.openStatus(), result.deadLine()),
                                wishCheck(result.articleId(), memberId),
                                result.imageKey().equals("")? "" : getS3Url(result.imageKey()).preSignedUrl(),
                                validateUser(result.writerId()).getNickname(),
                                validateUser(result.writerId()).getProfileImgUrl()
                        ))
                .collect(Collectors.toList());



        return searchArticles;
    }



    private void validateCheckOpenStatus(Article article) {
        if(article.getCurrentPerson() >= article.getMinPerson()){
            article.updateOpenStatus(OpenStatus.CONFIRMED_STATUS);
        }
    }

    private OpenStatus validateCheckDeadlineStatus(Long articleId, OpenStatus status, Date deadline) {
        Article article = articleRepository.findByArticleId(articleId)
                .orElseThrow(() -> new NotFoundException("Article", articleId));

        Timestamp deadlineTime = new Timestamp(deadline.getTime());
        if(deadlineTime.before(new Timestamp(System.currentTimeMillis()))){
            if(status.equals(OpenStatus.CONFIRMED_STATUS)) {
                article.updateOpenStatus(OpenStatus.DEADLINE_STATUS);
                return OpenStatus.DEADLINE_STATUS;
            }else{
                article.updateOpenStatus(OpenStatus.CANCELED_STATUS);
                return OpenStatus.CANCELED_STATUS;
            }
        }
        return status;
    }

    private Location validateLocation(Long locationId) {
        return locationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new NotFoundException("Location", locationId));
    }

    private GetS3UrlDto getS3Url(String imageKey){
        return imageGetUserCase.getGetS3Url(imageKey);
    }

    private Member validateUser(Long userId) {
        Member memerEntity = memberJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Member", userId));

        return memerEntity;
    }

    private boolean wishCheck(Long articleId, Long memberId){
        if(memberId == null){
            return false;
        }else{
            if(wishRepository.findByArticleId(articleId, memberId)){
                return true;
            }else {
                return false;
            }
        }
    }
}
