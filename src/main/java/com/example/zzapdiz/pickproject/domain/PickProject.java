package com.example.zzapdiz.pickproject.domain;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class PickProject {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long pickProjectId;

    @JoinColumn(name = "memberId")
    @ManyToOne
    private Member member;

    @JoinColumn(name = "fundingProjectId")
    @ManyToOne
    private FundingProject fundingProject;

}
