package com.example.zzapdiz.dofundproject.repository;

import com.example.zzapdiz.dofundproject.response.DoFundPhase1ResponseDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoFundPhase1Repository extends CrudRepository<DoFundPhase1ResponseDto, Long> {
    Optional<List<DoFundPhase1ResponseDto>> findAllByMemberIdAndFundingProjectId(Long memberId, Long projectId);
    Optional<DoFundPhase1ResponseDto> findByFundingProjectId(Long projectId);
}
