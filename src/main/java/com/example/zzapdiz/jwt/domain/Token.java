package com.example.zzapdiz.jwt.domain;

import com.example.zzapdiz.share.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Token extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long tokenId;

    @Column(nullable = false)
    private String grantType;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    @Column
    private Long memberId;
}
