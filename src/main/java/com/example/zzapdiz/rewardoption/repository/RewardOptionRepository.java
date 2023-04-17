package com.example.zzapdiz.rewardoption.repository;

import com.example.zzapdiz.rewardoption.domain.RewardOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardOptionRepository extends JpaRepository<RewardOption, Long> {
}
