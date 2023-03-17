package com.example.zzapdiz.reward.repository;

import com.example.zzapdiz.reward.response.RewardCreateResponseDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardRedisRepository extends CrudRepository<RewardCreateResponseDto, Long> {
    Optional<RewardCreateResponseDto> findById(Long id);

}
