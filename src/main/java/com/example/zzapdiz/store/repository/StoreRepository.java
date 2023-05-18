package com.example.zzapdiz.store.repository;

import com.example.zzapdiz.store.domain.StoreProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<StoreProject, Long> {
}
