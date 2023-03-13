package com.example.zzapdiz.reward;

import com.example.zzapdiz.share.RewardMakeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @Enumerated(EnumType.STRING)
    private RewardMakeType rewardMakeType; // 리워드 제작 유형

    @Column(nullable = false)
    private String rewardTitle; // 리워드 명

    @Column(nullable = false)
    private int rewardAmount; // 리워드 제한 수량

    @Column(nullable = false)
    private int rewardQuantity; // 리워드 금액

    @Column(nullable = false)
    private String rewardContent; // 리워드 내용
}
