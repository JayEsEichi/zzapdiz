package com.example.zzapdiz.reward.domain;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.rewardoption.RewardOption;
import com.example.zzapdiz.share.project.RewardMakeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Reward {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long rewardId;

    @Column(nullable = false)
    private String rewardType; // 리워드 유형

    @Column(nullable = false)
    private String rewardMakeType; // 리워드 제작 유형

    @Column(nullable = false)
    private String rewardTitle; // 리워드 명

    @Column(nullable = false)
    private int rewardAmount; // 리워드 제한 수량

    @Column(nullable = false)
    private int rewardQuantity; // 리워드 금액

    @Column(nullable = false)
    private String rewardContent; // 리워드 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundingProjectId")
    private FundingProject fundingProject;

    @JsonIgnore
    @OneToMany(mappedBy = "reward", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RewardOption> rewardOptions;
}
