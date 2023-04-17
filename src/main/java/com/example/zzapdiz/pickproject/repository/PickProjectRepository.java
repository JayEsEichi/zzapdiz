package com.example.zzapdiz.pickproject.repository;

import com.example.zzapdiz.pickproject.domain.PickProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickProjectRepository extends JpaRepository<PickProject, Long> {
}
