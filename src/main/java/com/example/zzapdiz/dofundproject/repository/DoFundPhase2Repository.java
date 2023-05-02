package com.example.zzapdiz.dofundproject.repository;

import com.example.zzapdiz.dofundproject.response.DoFundPhase2ResponseDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoFundPhase2Repository extends CrudRepository<DoFundPhase2ResponseDto, Long> {
    Optional<DoFundPhase2ResponseDto> findAllByMemberId(Long memberId);
}
