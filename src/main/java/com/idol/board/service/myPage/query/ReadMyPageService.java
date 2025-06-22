package com.idol.board.service.myPage.query;

import com.idol.board.domain.entity.Article;
import com.idol.board.domain.entity.Comment;
import com.idol.board.domain.entity.Location;
import com.idol.board.domain.entity.Participant;
import com.idol.board.dto.response.article.ArticleListResponseDto;
import com.idol.board.dto.response.comment.CommentQuestionResponseDto;
import com.idol.board.dto.response.comment.CommentResponseDto;
import com.idol.board.dto.response.mypage.MyPageQuestionResponseDto;
import com.idol.board.dto.response.mypage.UserAnswerResponseDto;
import com.idol.board.dto.response.mypage.UserAnswerTotalResponseDto;
import com.idol.board.dto.response.mypage.UserDataResponseDto;
import com.idol.board.repository.article.ArticleRepository;
import com.idol.board.repository.comment.CommentRepository;
import com.idol.board.repository.fanFal.ParticipantRepository;
import com.idol.board.repository.location.LocationRepository;
import com.idol.board.repository.mapper.ArticleReadAnswerQueryResult;
import com.idol.board.repository.mapper.CommentReadAnswerQueryResult;
import com.idol.board.repository.mapper.CommentReadQueryResult;
import com.idol.board.repository.mapper.CommentReadQuestionQueryResult;
import com.idol.board.usecase.mypage.query.ReadMyPageUseCase;
import com.idol.domains.member.domain.Member;
import com.idol.domains.member.repository.MemberJpaRepository;
import com.idol.domains.wish.usecase.CountWishUsecase;
import com.idol.global.exception.NotFoundException;
import com.idol.imageUpload.dto.GetS3UrlDto;
import com.idol.imageUpload.useCase.ImageGetUserCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReadMyPageService implements ReadMyPageUseCase {
    private final ArticleRepository articleRepository;
    private final LocationRepository locationRepository;
    private final ImageGetUserCase imageGetUserCase;
    private final ParticipantRepository participantRepository;
    private final CommentRepository commentRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final CountWishUsecase countWishUsecase;

    @Override
    public List<ArticleListResponseDto> readMypageList(Long limit, Long page, Long userId) {
        List<ArticleListResponseDto> searchMyPageList = articleRepository.findMyPageArticle(userId, limit, (page -1) * limit)
                .stream().map(result ->
                        ArticleListResponseDto.from(
                                result,
                                validateLocation(result.locationId()).getRoadNameAddress(),
                                getS3Url(result.imageKey())
                        ))
                .collect(Collectors.toList());

        return searchMyPageList;
    }


    @Override
    public List<ArticleListResponseDto> readJoinMypageList(Long limit, Long page, Long userId) {
        List<Participant> participants = participantRepository.findParticipantFromWriterId(userId);

        List<Long> articleIds = participants.stream().map(Participant::getArticleId).collect(Collectors.toList());


        List<ArticleListResponseDto> searchMyPageList = articleRepository.findJoinMyPageArticle(articleIds, limit, (page -1) * limit)
                .stream().map(result ->
                        ArticleListResponseDto.from(
                                result,
                                validateLocation(result.locationId()).getRoadNameAddress(),
                                getS3Url(result.imageKey())
                        ))
                .collect(Collectors.toList());

        return searchMyPageList;
    }

    @Override
    public UserDataResponseDto readUserInformation(Long userId) {
        Member member = memberJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Member", userId));

        Long wishCount = countWishUsecase.countWishesByMemberId(userId);

        UserDataResponseDto responseDto = UserDataResponseDto.from(
                member,
                getS3Url(member.getProfileImgUrl()),
                wishCount.intValue()
        );
        return responseDto;
    }

    @Override
    public UserAnswerTotalResponseDto readAllAnswers(Long lastArticleId, Long userId) {

        List<UserAnswerResponseDto> userAnswerResponseDtos = new ArrayList<>();

        List<ArticleReadAnswerQueryResult> answerArticles = lastArticleId == null?
                articleRepository.findAllByWriterIdInfiniteScrollFromArticle(userId) :
                articleRepository.findAllByWriterIdInfiniteScrollFromArticle(lastArticleId, userId);

        for(ArticleReadAnswerQueryResult articles : answerArticles){
            List<CommentReadAnswerQueryResult> answerComments  =
                    commentRepository.findAllByArticleId(articles.articleId());

            for(CommentReadAnswerQueryResult comments : answerComments){
                if(comments.commentId().equals(comments.parentCommentId())){
                    System.out.println(commentRepository.parentIdCountBy(comments.parentCommentId()));
                    if(commentRepository.parentIdCountBy(comments.parentCommentId()) == 2){
                        UserAnswerResponseDto dto = UserAnswerResponseDto.from(
                                articles.articleId(), articles.title(), articles.locationAddress(),
                                comments.createdAt(),
                                comments.content(),
                                true);
                        userAnswerResponseDtos.add(dto);
                    }else{
                        UserAnswerResponseDto dto = UserAnswerResponseDto.from(
                                articles.articleId(), articles.title(), articles.locationAddress(),
                                comments.createdAt(),
                                comments.content(),
                                false);
                        userAnswerResponseDtos.add(dto);
                    }
                }
            }
        }

        return new UserAnswerTotalResponseDto(userAnswerResponseDtos.size(),userAnswerResponseDtos);
    }


    private Location validateLocation(Long locationId) {
        return locationRepository.findByLocationId(locationId)
                .orElseThrow(() -> new NotFoundException("Location", locationId));
    }

    private String getS3Url(String imageKey){
        String userImageUrl = "";
        if(!imageKey.equals("")){
            userImageUrl = imageGetUserCase.getGetS3Url(imageKey).preSignedUrl();
        }

        return userImageUrl;
    }
}
