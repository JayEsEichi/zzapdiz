package com.example.zzapdiz.rewardoption.domain;

import com.example.zzapdiz.reward.domain.Reward;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class RewardOption {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long rewardOptionid;

    @Column(nullable = false)
    private String optionContent;

    @JsonIgnore
    @JoinColumn(name = "rewardId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Reward reward;

}
