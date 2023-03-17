package com.example.zzapdiz.fundingproject.repository;

import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase2ResponseDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Phase2RedisRepository extends CrudRepository<FundingProjectCreatePhase2ResponseDto, Long> {
    Optional<FundingProjectCreatePhase2ResponseDto> findById(Long id);
}
