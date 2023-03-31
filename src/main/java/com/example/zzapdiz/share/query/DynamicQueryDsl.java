package com.example.zzapdiz.share.query;

import com.example.zzapdiz.configuration.MySqlCustomTemplate;
import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.member.request.MemberFindRequestDto;
import com.example.zzapdiz.reward.domain.Reward;
import com.example.zzapdiz.reward.response.RewardInfoDto;
import com.example.zzapdiz.share.media.Media;
import com.example.zzapdiz.share.project.ProjectCategory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.MathExpressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;

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
     * **/
    public List<FundingProject> getMakersOtherProjects(Member maker, Long projectId){
        List<FundingProject> makersOtherProjects = jpaQueryFactory
                .selectFrom(fundingProject)
                .where(fundingProject.member.eq(maker)
                        .and(fundingProject.fundingProjectId.ne(projectId)))
                .limit(4)
                .fetch();

        return makersOtherProjects;
    }
}
