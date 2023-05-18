package com.example.zzapdiz.satisfaction.repository;

import com.example.zzapdiz.satisfaction.domain.Satisfaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SatisfactionRepository extends JpaRepository<Satisfaction,Long> {
}
