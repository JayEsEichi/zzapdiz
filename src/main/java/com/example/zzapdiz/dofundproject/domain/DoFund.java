package com.example.zzapdiz.dofundproject.domain;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.reward.domain.Reward;
import com.example.zzapdiz.share.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class DoFund extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long doFundId;

    @Column(nullable = false)
    private int doFundQuantity;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column
    private int point;

    @Column
    private int donation;

    @JoinColumn(name = "memberId")
    @ManyToOne
    private Member member;

    @JoinColumn(name = "rewardId")
    @ManyToOne
    private Reward reward;

    @JoinColumn(name = "fundingProjectId")
    @ManyToOne
    private FundingProject fundingProject;
}
