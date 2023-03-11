package com.example.zzapdiz.jwt.repository;

import com.example.zzapdiz.jwt.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
}
