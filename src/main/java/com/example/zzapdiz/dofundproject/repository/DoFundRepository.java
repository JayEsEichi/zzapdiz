package com.example.zzapdiz.dofundproject.repository;

import com.example.zzapdiz.dofundproject.domain.DoFund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoFundRepository extends JpaRepository<DoFund, Long> {
}
