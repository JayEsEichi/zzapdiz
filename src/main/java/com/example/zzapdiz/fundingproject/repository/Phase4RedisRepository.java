package com.example.zzapdiz.fundingproject.repository;

import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase4ResponseDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Phase4RedisRepository extends CrudRepository<FundingProjectCreatePhase4ResponseDto, Long> {
    Optional<FundingProjectCreatePhase4ResponseDto> findById(Long id);
}
