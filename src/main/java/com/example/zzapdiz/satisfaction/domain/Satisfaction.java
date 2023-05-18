package com.example.zzapdiz.satisfaction.domain;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Satisfaction {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long satisfactionId;

    @Column(nullable = false)
    private String satisfactionContent;

    @Column(nullable = false)
    private String satisfactionStarRate;

    @JsonIgnore
    @JoinColumn(name = "memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JsonIgnore
    @JoinColumn(name = "fundingProjectId")
    @ManyToOne(fetch = FetchType.LAZY)
    private FundingProject fundingProject;
}
