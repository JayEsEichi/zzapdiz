package com.example.zzapdiz.fundingproject.repository;

import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase1ResponseDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Phase1RedisRepository extends CrudRepository<FundingProjectCreatePhase1ResponseDto, Long> {
    Optional<FundingProjectCreatePhase1ResponseDto> findById(Long id);

}
