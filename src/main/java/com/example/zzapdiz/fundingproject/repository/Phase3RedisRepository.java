package com.example.zzapdiz.fundingproject.repository;

import com.example.zzapdiz.fundingproject.response.FundingProjectCreatePhase3ResponseDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Phase3RedisRepository extends CrudRepository<FundingProjectCreatePhase3ResponseDto, Long> {
    Optional<FundingProjectCreatePhase3ResponseDto> findById(Long id);
}
