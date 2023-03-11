package com.example.zzapdiz.share;

import com.example.zzapdiz.member.domain.Member;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.example.zzapdiz.member.domain.QMember.member;
import static com.example.zzapdiz.jwt.domain.QToken.token;

@Slf4j
@RequiredArgsConstructor
@Repository
public class DynamicQueryDsl {

    private final JPAQueryFactory jpaQueryFactory;


//    public List<GameRoom> findGameRooms(String condition) {
//        return jpaQueryFactory
//                .selectFrom(gameRoom)
//                .where(eqMode(condition))
//                .orderBy(gameRoom.createdAt.desc())
//                .fetch();
//    }
//
//    private BooleanExpression eqMode(String condition) {
//        if(condition.equals("normal")){
//            return gameRoom.mode.eq(Mode.일반);
//        }else if(condition.equals("fool")){
//            return gameRoom.mode.eq(Mode.바보);
//        }else if(condition.equals("wait")){
//            return gameRoom.status.eq("wait");
//        }else if(condition.equals("start")){
//            return gameRoom.status.eq("start");
//        }
//        return null;
//    }

    /** 이메일로 회원 조회 **/
    public Member findMemberByEmail(String email){
        Member loginMember = jpaQueryFactory
                .selectFrom(member)
                .where(member.email.eq(email))
                .fetchOne();

        return loginMember;
    }

    /** 재로그인 진행 시 기존에 남아있던 토큰 삭제 처리 **/
    @Transactional
    public void reLoginDeleteToken(Long memberId){
        jpaQueryFactory
                .delete(token)
                .where(token.memberId.eq(memberId))
                .execute();
    }

}
