package com.example.zzapdiz.share;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DynamicQueryDsl {
    private final JPAQueryFactory jpaQueryFactory;

    // 게임방 조회
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

}
