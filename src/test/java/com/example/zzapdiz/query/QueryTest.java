//package com.example.zzapdiz.query;
//
//import com.example.zzapdiz.configuration.MySqlCustomTemplate;
//import com.example.zzapdiz.fundingproject.domain.FundingProject;
//import com.example.zzapdiz.fundingproject.repository.FundingProjectRepository;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static com.example.zzapdiz.fundingproject.domain.QFundingProject.fundingProject;
//
//@DataJpaTest
//@Import(QueryTestConfig.class)
////@ExtendWith(MockitoExtension.class)
//public class QueryTest {
//
////    @Mock
////    private EntityManagerFactory entityManagerFactory;
//
//    @Autowired
//    private JPAQueryFactory jpaQueryFactory;
//
//    @Autowired
//    private FundingProjectRepository fundingProjectRepository;
//
//    /**
//     * 현재 조회하고 있는 프로젝트와 동일한 카테고리를 가진 비슷한 프로젝트 4개 페이징 처리 목록
//     **/
//    @DisplayName("커스텀 JPQLTemplate + 랜덤 레코드 추출 테스트")
//    @Test
//    void getSimilarFundingProjects() {
//
//        fundingProjectRepository.save(getFakeProject(1L));
//        fundingProjectRepository.save(getFakeProject(2L));
//        fundingProjectRepository.save(getFakeProject(3L));
//        fundingProjectRepository.save(getFakeProject(4L));
//        fundingProjectRepository.save(getFakeProject(5L));
//        fundingProjectRepository.save(getFakeProject(6L));
//        fundingProjectRepository.save(getFakeProject(7L));
//
////        EntityManager entityManager = entityManagerFactory.createEntityManager();
////        MySqlCustomTemplate mySqlCustomTemplate = new MySqlCustomTemplate();
////        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory();
//
//        List<FundingProject> similarProjects = jpaQueryFactory
//                .selectFrom(fundingProject)
//                .where(fundingProject.projectCategory.eq("전자/가전")
//                        .and(fundingProject.fundingProjectId.ne(1L)))
////                .orderBy(NumberExpression.random().asc())
//                .limit(4)
//                .fetch();
//
//        for (FundingProject project : similarProjects) {
//            System.out.println("프로젝트 랜덤 추출 확인 " + project.getFundingProjectId());
//        }
//    }
//
//
//    private FundingProject getFakeProject(Long id) {
//        return FundingProject.builder()
//                .fundingProjectId(id)
//                .projectTitle("테스트용 프로젝트명")
//                .projectCategory("전자/가전")
//                .projectType("손수 제작")
//                .achievedAmount(90000000)
//                .adultCheck("X")
//                .searchTag("#검색태그")
//                .storyText("프로젝트 스토리~~")
//                .projectDescript("프로젝트 요약 설명~~")
//                .openReservation("X")
//                .startDate(LocalDateTime.now())
//                .endDate(LocalDateTime.now())
//                .makerType("PERSONAL")
//                .progress("진행중")
//                .deliveryCheck("X")
//                .build();
//    }
//}
