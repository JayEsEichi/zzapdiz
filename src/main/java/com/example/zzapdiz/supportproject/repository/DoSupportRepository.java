package com.example.zzapdiz.supportproject.repository;

import com.example.zzapdiz.supportproject.domain.DoSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoSupportRepository extends JpaRepository<DoSupport, Long> {
}
