package com.example.zzapdiz.share.query;

import com.example.zzapdiz.configuration.MySqlCustomTemplate;
import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.fundingproject.request.FundingProjectUpdateRequestDto;
import com.example.zzapdiz.fundingproject.response.ProjectsReadResponseDto;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.member.request.MemberFindRequestDto;
import com.example.zzapdiz.reward.domain.Reward;
import com.example.zzapdiz.share.media.Media;
import com.example.zzapdiz.share.media.MediaUploadInterface;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.zzapdiz.member.domain.QMember.member;
import static com.example.zzapdiz.jwt.domain.QToken.token;
import static com.example.zzapdiz.share.media.QMedia.media;
import static com.example.zzapdiz.fundingproject.domain.QFundingProject.fundingProject;
import static com.example.zzapdiz.pickproject.domain.QPickProject.pickProject;
import static com.example.zzapdiz.supportproject.domain.QDoSupport.doSupport;
import static com.example.zzapdiz.reward.domain.QReward.reward;

@Slf4j
@RequiredArgsConstructor
@Repository
public class DynamicQueryDsl {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;
    private final MediaUploadInterface mediaUploadInterface;

    /**
     * 조건에 따른 회원 정보 조회 동적 쿼리
     **/
    public Member findMemberByAnyCondition(MemberFindRequestDto memberFindRequestDto) {
        return jpaQueryFactory
                .selectFrom(member)
                .where(eqMemberId(memberFindRequestDto)
                        .and(eqEmail(memberFindRequestDto))
                        .and(eqMemberName(memberFindRequestDto)))
                .fetchOne();
    }

    /**
     * 계정 ID에 따른 조회 조건
     **/
    private BooleanExpression eqMemberId(MemberFindRequestDto memberFindRequestDto) {
        if (memberFindRequestDto.getMemberId() > 0L) {
            return member.memberId.eq(memberFindRequestDto.getMemberId());
        }
        return null;
    }

    /**
     * 계정 이메일에 따른 조회 조건
     **/
    private BooleanExpression eqEmail(MemberFindRequestDto memberFindRequestDto) {
        if (memberFindRequestDto.getEmail() != null) {
            return member.email.eq(memberFindRequestDto.getEmail());
        }
        return null;
    }

    /**
     * 계정 회원 이름에 따른 조회 조건
     **/
    private BooleanExpression eqMemberName(MemberFindRequestDto memberFindRequestDto) {
        if (memberFindRequestDto.getMemberName() != null) {
            return member.memberName.eq(memberFindRequestDto.getMemberName());
        }
        return null;
    }


    /**
     * 이메일로 회원 조회
     **/
    public Member findMemberByEmail(String email) {
        Member loginMember = jpaQueryFactory
                .selectFrom(member)
                .where(member.email.eq(email))
                .fetchOne();

        return loginMember;
    }

    /**
     * 토큰 삭제 처리
     **/
    @Transactional
    public void deleteToken(Long memberId) {
        jpaQueryFactory
                .delete(token)
                .where(token.memberId.eq(memberId))
                .execute();

        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 회원탈퇴 시 회원 삭제
     **/
    @Transactional
    public void deleteMember(Long memberId) {
        jpaQueryFactory
                .delete(member)
                .where(member.memberId.eq(memberId))
                .execute();

        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 미디어 삭제
     **/
    @Transactional
    public void deleteMedia(MultipartFile requestMedia) {
        jpaQueryFactory
                .delete(media)
                .where(media.mediaRealName.eq(requestMedia.getOriginalFilename()))
                .execute();

        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 프로젝트에 속한 미디어들 조회
     **/
    public List<Media> findMedias(String projectTitle) {
        return jpaQueryFactory
                .selectFrom(media)
                .where(media.projectTitle.eq(projectTitle))
                .fetch();
    }

    /**
     * 미디어가 속한 프로젝트 id 업데이트
     **/
    @Transactional
    public void updateMedia(Media updateMedia, Long fundingProjectId) {
        jpaQueryFactory
                .update(media)
                .set(media.fundingProjectId, fundingProjectId)
                .where(media.mediaId.eq(updateMedia.getMediaId()))
                .execute();

        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 펀딩 프로젝트 조회
     **/
    public FundingProject getFundingProject(Long projectId) {
        return jpaQueryFactory
                .selectFrom(fundingProject)
                .where(fundingProject.fundingProjectId.eq(projectId))
                .fetchOne();
    }


    /**
     * 찜하기 두번 활성화 시 취소
     **/
    @Transactional
    public Boolean pickCancel(Member authMember, FundingProject project) {

        if (jpaQueryFactory
                .selectFrom(pickProject)
                .where(pickProject.member.eq(authMember).and(pickProject.fundingProject.eq(project)))
                .fetchOne() != null) {
            jpaQueryFactory
                    .delete(pickProject)
                    .where(pickProject.member.eq(authMember).and(pickProject.fundingProject.eq(project)))
                    .execute();

            entityManager.flush();
            entityManager.clear();

            return true;
        }

        return false;
    }

    /**
     * FundingProject 엔티티에 지지 수 업데이트
     **/
    @Transactional
    public synchronized void supportCountUpdate(FundingProject supportProject) {
        Long sptCnt = jpaQueryFactory
                .select(doSupport.count())
                .from(doSupport)
                .where(doSupport.fundingProject.eq(supportProject))
                .fetchOne();

        int supportCount = sptCnt.intValue();

        jpaQueryFactory
                .update(fundingProject)
                .set(fundingProject.supportCount, supportCount)
                .where(fundingProject.fundingProjectId.eq(supportProject.getFundingProjectId()))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE) // 비관적 락을 걸어 지지수 업데이트 동기화
                .execute();

        entityManager.flush();
        entityManager.clear();
    }

    /**
     * FundingProject 엔티티에 찜하기 수 업데이트
     **/
    @Transactional
    public synchronized void pickCountUpdate(FundingProject pickFundingProject) {
        Long pickCnt = jpaQueryFactory
                .select(pickProject.count())
                .from(pickProject)
                .where(pickProject.fundingProject.eq(pickFundingProject))
                .fetchOne();

        int pickCount = pickCnt.intValue();

        jpaQueryFactory
                .update(fundingProject)
                .set(fundingProject.pickCount, pickCount)
                .where(fundingProject.fundingProjectId.eq(pickFundingProject.getFundingProjectId()))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE) // 비관적 락을 걸어 지지수 업데이트 동기화
                .execute();

        entityManager.flush();
        entityManager.clear();
    }


    /**
     * 1. 프로젝트 찜하기도 우선 FundingProject 엔티티에 Count 컬럼 하나 만들어서 반영되게끔 추가 업데이트
     * 2. 컬럼 속성 하나 만들고 펀딩 프로젝트 목록 조회 api 로직 재구성 필요!
     * **/


    /**
     * 지지하기 두번 활성화 시 취소
     **/
    @Transactional
    public Boolean supportCancel(Member authMember, FundingProject project) {

        if (jpaQueryFactory
                .selectFrom(doSupport)
                .where(doSupport.member.eq(authMember).and(doSupport.fundingProject.eq(project)))
                .fetchOne() != null) {
            jpaQueryFactory
                    .delete(doSupport)
                    .where(doSupport.member.eq(authMember).and(doSupport.fundingProject.eq(project)))
                    .execute();

            entityManager.flush();
            entityManager.clear();

            return true;
        }

        return false;
    }


    /**
     * 저장된 미디어 데이터들 조회
     **/
    public List<Media> getMedias(Long projectId) {
        return jpaQueryFactory
                .selectFrom(media)
                .where(media.fundingProjectId.eq(projectId))
                .fetch();
    }


    /**
     * 프로젝트 찜한 수
     **/
    public Long countPick(FundingProject readProject) {
        return jpaQueryFactory
                .select(pickProject.count())
                .from(pickProject)
                .where(pickProject.fundingProject.eq(readProject))
                .fetchOne();
    }


    /**
     * 프로젝트 지지 수
     **/
    public Long countSupport(FundingProject readProject) {
        return jpaQueryFactory
                .select(doSupport.count())
                .from(doSupport)
                .where(doSupport.fundingProject.eq(readProject))
                .fetchOne();
    }


    /**
     * 프로젝트에 속한 리워드들 조회
     **/
    public List<Reward> getRewards(FundingProject readProject) {
        List<Reward> rewardsList = jpaQueryFactory
                .selectFrom(reward)
                .where(reward.fundingProject.eq(readProject))
                .fetch();

//        List<RewardInfoDto> rewards = new ArrayList<>();
//
//        for(Reward eachReward : rewardsList){
//            rewards.add(RewardInfoDto.builder()
//                    .rewardQuantity(eachReward.))
//        }

        return rewardsList;
    }

    /**
     * 현재 조회하고 있는 프로젝트와 동일한 카테고리를 가진 비슷한 프로젝트 4개 페이징 처리 목록
     * !! 현재 랜덤으로 뽑혀야 되는 부분은 작동되지 않고 있다. 추후 반영 필요
     **/
    public List<FundingProject> getSimilarFundingProjects(FundingProject readFundingProject) {

        MySqlCustomTemplate mySqlCustomTemplate = new MySqlCustomTemplate();
        JPAQueryFactory jpaQueryFactory1 = new JPAQueryFactory(mySqlCustomTemplate, entityManager);

        List<FundingProject> similarProjects = jpaQueryFactory1
                .selectFrom(fundingProject)
                .where(fundingProject.projectCategory.eq(readFundingProject.getProjectCategory())
                        .and(fundingProject.fundingProjectId.ne(readFundingProject.getFundingProjectId())))
                .limit(4)
                .fetch();

//        int randomPlace = (int)(Math.random() * Integer.parseInt(countSimilarProjects.toString()) + 1);

        return similarProjects;
    }


    /**
     * 현재 조회하고 있는 프로젝트의 메이커가 만든 다른 프로젝트 목록 4개 구성
     * !! 현재 랜덤으로 뽑혀야 되는 부분은 작동되지 않고 있다. 추후 반영 필요
     **/
    public List<FundingProject> getMakersOtherProjects(Member maker, Long projectId) {
        List<FundingProject> makersOtherProjects = jpaQueryFactory
                .selectFrom(fundingProject)
                .where(fundingProject.member.eq(maker)
                        .and(fundingProject.fundingProjectId.ne(projectId)))
                .limit(4)
                .fetch();

        return makersOtherProjects;
    }


    /**
     * 펀딩 프로젝트 리스트 조회
     **/
    public List<ProjectsReadResponseDto> getProjectList(String projectCategory, String progress, String orderBy, int page) {
        // 스크롤할 때마다 추출될 프로젝트 개수
        int listInPage = 12 * page;
        // 프로젝트 목록을 담기 위한 Dto 리스트 객체
        List<ProjectsReadResponseDto> projects = new ArrayList<>();

        // 조건에 맞는 펀딩 프로젝트 리스트 목록
        List<FundingProject> fundingProjects = jpaQueryFactory
                .selectFrom(fundingProject)
                .where(eqCategory(projectCategory).and(eqProgress(progress)))
                .orderBy(eqOrderBy(orderBy))
                .limit(listInPage)
                .fetch();

        // 조회한 펀딩 프로젝트 목록들을 ResponseDto 리스트 객체에 담기
        for (FundingProject eachProject : fundingProjects) {
            projects.add(ProjectsReadResponseDto.builder()
                    .fundingProjectId(eachProject.getFundingProjectId())
                    .projectTitle(eachProject.getProjectTitle())
                    .reachPercentage("") // 펀딩하기 기능 구축 후 다시 업데이트
                    .reachQuantity("")  // 펀딩하기 기능 구축 후 다시 업데이트
                    .remainProjectDate((eachProject.getEndDate().getDayOfYear() - LocalDateTime.now().getDayOfYear()) + "일 남음.")
                    .thumbnailImage(jpaQueryFactory
                            .select(media.mediaUrl)
                            .from(media)
                            .where(media.fundingProjectId.eq(eachProject.getFundingProjectId())
                                    .and(media.mediaPurpose.eq("thumb")))
                            .fetchOne())
                    .build());
        }

        return projects;
    }

    /**
     * 프로젝트 카테고리 BooleanExpression
     **/
    private BooleanExpression eqCategory(String projectCategory) {
        if (projectCategory != null) {
            return fundingProject.projectCategory.eq(projectCategory);
        }
        return null;
    }


    /**
     * 프로젝트 진행상황 BooleanExpression
     **/
    private BooleanExpression eqProgress(String progress) {
        if (progress != null) {
            return fundingProject.progress.eq(progress);
        }
        return null;
    }


    /**
     * 프로젝트 정렬기준 BooleanExpression
     * (1) 추천순 : 지지수가 많은 프로젝트부터 우선적으로 조회. ⇒ recommend
     * (2) 인기순 : 달성율 기준으로 가장 초과 달성된 프로젝트부터 우선적으로 조회. ⇒ like
     * (3) 모집금액순 : 달성율 + 모집된 금액이 가장 큰 프로젝트부터 우선적으로 조회. ⇒ quantity
     * (4) 마감임박순 : 프로젝트 잔여일이 가장 적은 프로젝트부터 우선적으로 조회. ⇒ closeend
     * (5) 최신순 : 프로젝트 생성일 최신순으로 조회. ⇒ recent
     **/
    private OrderSpecifier eqOrderBy(String orderBy) {
        if (orderBy.equals("추천순")) {
            return fundingProject.supportCount.desc();
        } else if (orderBy.equals("인기순")) {
            // 펀딩하기 기능 구축 후 업데이트 필요
        } else if (orderBy.equals("모집금액순")) {
            // 펀딩하기 기능 구축 후 업데이트 필요
        } else if (orderBy.equals("마감임박순")) {

        }
        return fundingProject.createdAt.desc();
    }

    /**
     * 프로젝트의 현재까지 달성된 금액 정보 조회
     **/
    public Integer getCollectQuantity(Long projectId) {
        return jpaQueryFactory
                .select(fundingProject.collectQuantity)
                .from(fundingProject)
                .where(fundingProject.fundingProjectId.eq(projectId))
                .fetchOne();
    }

    /**
     * 프로젝트 정보 수정
     **/
    @Transactional
    public void updateProjectInfo(FundingProjectUpdateRequestDto fundingProjectUpdateRequestDto, FundingProject updateProject) {

        // 프로젝트 타이틀 명이 변경되었다면 수정
        if (fundingProjectUpdateRequestDto.getProjectTitle() != null && !fundingProjectUpdateRequestDto.getProjectTitle().equals(updateProject.getProjectTitle())) {
            jpaQueryFactory
                    .update(fundingProject)
                    .set(fundingProject.projectTitle, fundingProjectUpdateRequestDto.getProjectTitle())
                    .where(fundingProject.fundingProjectId.eq(fundingProjectUpdateRequestDto.getProjectId()))
                    .execute();
        }

        // 프로젝트 목표 금액 정보가 변경되었다면 수정
        if (fundingProjectUpdateRequestDto.getAchievedAmount() != 0 && !(fundingProjectUpdateRequestDto.getAchievedAmount() == updateProject.getAchievedAmount())) {
            jpaQueryFactory
                    .update(fundingProject)
                    .set(fundingProject.achievedAmount, fundingProjectUpdateRequestDto.getAchievedAmount())
                    .where(fundingProject.fundingProjectId.eq(fundingProjectUpdateRequestDto.getProjectId()))
                    .execute();
        }

        // 프로젝트 검색 태그 정보가 변경되었다면 수정
        if (fundingProjectUpdateRequestDto.getSearchTag() != null && !fundingProjectUpdateRequestDto.getSearchTag().equals(updateProject.getSearchTag())) {
            jpaQueryFactory
                    .update(fundingProject)
                    .set(fundingProject.searchTag, fundingProjectUpdateRequestDto.getSearchTag())
                    .where(fundingProject.fundingProjectId.eq(fundingProjectUpdateRequestDto.getProjectId()))
                    .execute();
        }

        // 프로젝트 스토리 글이 변경되었다면 수정
        if (fundingProjectUpdateRequestDto.getStoryText() != null && !fundingProjectUpdateRequestDto.getStoryText().equals(updateProject.getStoryText())) {
            jpaQueryFactory
                    .update(fundingProject)
                    .set(fundingProject.storyText, fundingProjectUpdateRequestDto.getStoryText())
                    .where(fundingProject.fundingProjectId.eq(fundingProjectUpdateRequestDto.getProjectId()))
                    .execute();
        }

        // 프로젝트 상세 설명 정보가 변경되었다면 수정
        if (fundingProjectUpdateRequestDto.getProjectDescript() != null && !fundingProjectUpdateRequestDto.getProjectDescript().equals(updateProject.getProjectDescript())) {
            jpaQueryFactory
                    .update(fundingProject)
                    .set(fundingProject.projectDescript, fundingProjectUpdateRequestDto.getProjectDescript())
                    .where(fundingProject.fundingProjectId.eq(fundingProjectUpdateRequestDto.getProjectId()))
                    .execute();
        }

        // 프로젝트 종료일 정보가 변경되었다면 수정
        if (fundingProjectUpdateRequestDto.getEndDate() != null && !fundingProjectUpdateRequestDto.getEndDate().equals("")) {
            log.info("종료일 조건 미달성인데 해당 조건에 들어오게 될 경우");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime endDate = LocalDateTime.parse(fundingProjectUpdateRequestDto.getEndDate(), formatter);

            if (!updateProject.getEndDate().equals(endDate)) {
                jpaQueryFactory
                        .update(fundingProject)
                        .set(fundingProject.endDate, endDate)
                        .where(fundingProject.fundingProjectId.eq(fundingProjectUpdateRequestDto.getProjectId()))
                        .execute();
            }
        }

        // 배송 여부가 변경되었다면 수정
        if (fundingProjectUpdateRequestDto.getDeliveryCheck() != null && !fundingProjectUpdateRequestDto.getDeliveryCheck().equals(updateProject.getDeliveryCheck())) {
            jpaQueryFactory
                    .update(fundingProject)
                    .set(fundingProject.deliveryCheck, fundingProjectUpdateRequestDto.getDeliveryCheck())
                    .where(fundingProject.fundingProjectId.eq(fundingProjectUpdateRequestDto.getProjectId()))
                    .execute();
        }

        // 배송 가격이 변경되었다면 수정
        if (fundingProjectUpdateRequestDto.getDeliveryPrice() != 0 && !(fundingProjectUpdateRequestDto.getDeliveryPrice() == updateProject.getDeliveryPrice())) {
            jpaQueryFactory
                    .update(fundingProject)
                    .set(fundingProject.deliveryPrice, fundingProjectUpdateRequestDto.getDeliveryPrice())
                    .where(fundingProject.fundingProjectId.eq(fundingProjectUpdateRequestDto.getProjectId()))
                    .execute();
        }

        // 배송 시작일 정보가 변경 되었다면 수정
        if (fundingProjectUpdateRequestDto.getDeliveryStartDate() != null && !fundingProjectUpdateRequestDto.getDeliveryStartDate().equals("")) {
            log.info("배달 시작일 조건 미달성인데 해당 조건에 들어오게 될 경우");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime deliveryStartDate = LocalDateTime.parse(fundingProjectUpdateRequestDto.getDeliveryStartDate(), formatter);

            if (!updateProject.getDeliveryStartDate().equals(deliveryStartDate)) {
                jpaQueryFactory
                        .update(fundingProject)
                        .set(fundingProject.deliveryStartDate, deliveryStartDate)
                        .where(fundingProject.fundingProjectId.eq(fundingProjectUpdateRequestDto.getProjectId()))
                        .execute();
            }
        }

        entityManager.flush();
        entityManager.clear();
    }

    /**
     * 프로젝트에 속한 썸네일 이미지 수정
     **/
    @Transactional
    public void updateProjectThumbnail(FundingProject updateProject, MultipartFile thumbnailImage) {

        if (thumbnailImage != null) {
            jpaQueryFactory
                    .delete(media)
                    .where(media.fundingProjectId.eq(updateProject.getFundingProjectId())
                            .and(media.mediaPurpose.eq("thumb")))
                    .execute();

            mediaUploadInterface.uploadMedia(thumbnailImage, "thumb", updateProject.getProjectTitle());

            jpaQueryFactory
                    .update(media)
                    .set(media.fundingProjectId, updateProject.getFundingProjectId())
                    .where(media.projectTitle.eq(updateProject.getProjectTitle()).and(media.mediaPurpose.eq("thumb")))
                    .execute();

            entityManager.flush();
            entityManager.clear();
        }
    }

    /**
     * 프로젝트에 속한 비디오 및 이미지 수정
     **/
    @Transactional
    public void updateProjectMediaInfo(FundingProject updateProject, List<MultipartFile> videoAndImages) {

        if (videoAndImages.size() != 0) {
            List<String> mediaOriginalNameList = jpaQueryFactory
                    .select(media.mediaRealName)
                    .from(media)
                    .where(media.fundingProjectId.eq(updateProject.getFundingProjectId()))
                    .fetch();

            // s3에 저장되어 있는 이미지 정보들 삭제
            for(String eachMediaName : mediaOriginalNameList){
                mediaUploadInterface.deleteFile(eachMediaName);
            }

            jpaQueryFactory
                    .delete(media)
                    .where(media.fundingProjectId.eq(updateProject.getFundingProjectId())
                            .and(media.mediaPurpose.eq("story")))
                    .execute();

            // 새로 저장될 이미지 정보
            for (MultipartFile eachMultiPartFile : videoAndImages) {
                mediaUploadInterface.uploadMedia(eachMultiPartFile, "story", updateProject.getProjectTitle());
            }

            jpaQueryFactory
                    .update(media)
                    .set(media.fundingProjectId, updateProject.getFundingProjectId())
                    .where(media.projectTitle.eq(updateProject.getProjectTitle()).and(media.mediaPurpose.eq("story")))
                    .execute();

            entityManager.flush();
            entityManager.clear();
        }

    }

    /**
     * 프로젝트 진행 상황 변경
     */
    @Transactional
    public void updateProjectProgress(Long projectId){

        jpaQueryFactory
                .update(fundingProject)
                .set(fundingProject.progress, "종료")
                .where(fundingProject.fundingProjectId.eq(projectId))
                .execute();

        entityManager.flush();
        entityManager.clear();

    }

}
