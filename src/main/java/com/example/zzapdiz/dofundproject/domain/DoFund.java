package com.example.zzapdiz.dofundproject.domain;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.reward.domain.Reward;
import com.example.zzapdiz.share.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @Column(nullable = false)
    private Long fundingProjectId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy = "doFund", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reward> rewards;


}
