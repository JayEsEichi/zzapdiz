package com.example.zzapdiz.redis;

import com.example.zzapdiz.fundingproject.repository.Phase1RedisRepository;
import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase1ResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private Phase1RedisRepository phase1RedisRepository;

    @DisplayName("[RedisTest] Phase1 정보 확인")
    @Test
    void phase1test() {

        phase1RedisRepository.save(phase1ResponseDto());

        Optional<FundingProjectCreatePhase1ResponseDto> fundingProjectCreatePhase1ResponseDto = phase1RedisRepository.findById(phase1ResponseDto().getMemberId());

        System.out.println(fundingProjectCreatePhase1ResponseDto.get().getProjectType());
        System.out.println(phase1RedisRepository.count());
    }

    private FundingProjectCreatePhase1ResponseDto phase1ResponseDto() {
        return FundingProjectCreatePhase1ResponseDto.builder()
                .memberId(1L)
                .projectCategory("테크/가전")
                .projectType("ODM")
                .makerType("개인")
                .rewardType("인테리어")
                .rewardMakeType("ODM")
                .build();
    }
}
