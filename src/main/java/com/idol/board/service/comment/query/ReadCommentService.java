package com.idol.board.service.comment.query;

import com.idol.board.dto.response.comment.CommentResponseDto;
import com.idol.board.repository.comment.CommentRepository;
import com.idol.board.repository.mapper.CommentReadQueryResult;
import com.idol.board.usecase.comment.query.ReadCommentUseCase;
import com.idol.domains.member.domain.Member;
import com.idol.domains.member.repository.MemberJpaRepository;
import com.idol.global.exception.NotFoundException;
import com.idol.imageUpload.dto.GetS3UrlDto;
import com.idol.imageUpload.useCase.ImageGetUserCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadCommentService implements ReadCommentUseCase {
    private final CommentRepository commentJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ImageGetUserCase imageGetUserCase;

    @Transactional(readOnly = true)
    public List<CommentResponseDto> readAll(Long articleId, Long lastParentCommentId, Long lastCommentId, Long limit){
        List<CommentReadQueryResult> comments = lastParentCommentId == null || lastCommentId == null ?
                commentJpaRepository.findAllInfiniteScroll(articleId, limit) :
                commentJpaRepository.findAllInfiniteScroll(articleId,lastParentCommentId,lastCommentId,limit);

        List<CommentResponseDto> commentResponseDtos =  new  ArrayList<>();

        for(CommentReadQueryResult result : comments){
            Member member = memberJpaRepository.findById(result.writerId()).orElseThrow(() -> new NotFoundException("Member", result.writerId()));
            String writerImageUrl = null;
            if(member.getProfileImgUrl() != null){
                writerImageUrl = getS3Url(member.getProfileImgUrl()).preSignedUrl();
            }

            CommentResponseDto dto = CommentResponseDto.from(result,member.getNickname(),writerImageUrl);
            commentResponseDtos.add(dto);
        }


        return commentResponseDtos;
    }

    private GetS3UrlDto getS3Url(String imageKey){
        return imageGetUserCase.getGetS3Url(imageKey);
    }
}
