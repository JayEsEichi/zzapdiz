package com.example.zzapdiz.share.query;

import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.member.request.MemberFindRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;

import static com.example.zzapdiz.member.domain.QMember.member;
import static com.example.zzapdiz.jwt.domain.QToken.token;
import static com.example.zzapdiz.share.media.QMedia.media;

@Slf4j
@RequiredArgsConstructor
@Repository
public class DynamicQueryDsl {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;


    /** 조건에 따른 회원 정보 조회 동적 쿼리 **/
    public Member findMemberByAnyCondition(MemberFindRequestDto memberFindRequestDto){
        return jpaQueryFactory
                .selectFrom(member)
                .where(eqMemberId(memberFindRequestDto)
                        .and(eqEmail(memberFindRequestDto))
                        .and(eqMemberName(memberFindRequestDto)))
                .fetchOne();
    }

    /** 계정 ID에 따른 조회 조건 **/
    private BooleanExpression eqMemberId(MemberFindRequestDto memberFindRequestDto) {
        if(memberFindRequestDto.getMemberId() > 0L){
            return member.memberId.eq(memberFindRequestDto.getMemberId());
        }
        return null;
    }

    /** 계정 이메일에 따른 조회 조건 **/
    private BooleanExpression eqEmail(MemberFindRequestDto memberFindRequestDto) {
        if(memberFindRequestDto.getEmail() != null){
            return member.email.eq(memberFindRequestDto.getEmail());
        }
        return null;
    }

    /** 계정 회원 이름에 따른 조회 조건 **/
    private BooleanExpression eqMemberName(MemberFindRequestDto memberFindRequestDto) {
        if(memberFindRequestDto.getMemberName() != null){
            return member.memberName.eq(memberFindRequestDto.getMemberName());
        }
        return null;
    }


    /** 이메일로 회원 조회 **/
    public Member findMemberByEmail(String email){
        Member loginMember = jpaQueryFactory
                .selectFrom(member)
                .where(member.email.eq(email))
                .fetchOne();

        return loginMember;
    }

    /** 토큰 삭제 처리 **/
    @Transactional
    public void deleteToken(Long memberId){
        jpaQueryFactory
                .delete(token)
                .where(token.memberId.eq(memberId))
                .execute();

        entityManager.flush();
        entityManager.clear();
    }

    /** 회원탈퇴 시 회원 삭제 **/
    @Transactional
    public void deleteMember(Long memberId){
        jpaQueryFactory
                .delete(member)
                .where(member.memberId.eq(memberId))
                .execute();

        entityManager.flush();
        entityManager.clear();
    }

    /** 미디어 삭제 **/
    @Transactional
    public void deleteMedia(MultipartFile requestMedia){
        jpaQueryFactory
                .delete(media)
                .where(media.mediaRealName.eq(requestMedia.getOriginalFilename()))
                .execute();

        entityManager.flush();
        entityManager.clear();
    }


}