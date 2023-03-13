package com.example.zzapdiz.fundingproject.repository;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundingProjectRepository extends JpaRepository<FundingProject, Long> {
}
