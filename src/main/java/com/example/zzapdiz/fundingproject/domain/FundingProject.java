package com.example.zzapdiz.fundingproject.domain;

import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.pickproject.domain.PickProject;
import com.example.zzapdiz.reward.domain.Reward;
import com.example.zzapdiz.share.Timestamped;
import com.example.zzapdiz.supportproject.domain.DoSupport;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(indexes = {@Index(name = "indexId", columnList = "fundingProjectId")})
@Entity
public class FundingProject extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long fundingProjectId;

    @Column(nullable = false)
    private String projectTitle;

    @Column(nullable = false)
    private String projectCategory;

    @Column(nullable = false)
    private String projectType;

    @Column(nullable = false)
    private int achievedAmount;

    @Column(nullable = false)
    private String adultCheck;

    @Column(nullable = false)
    private String searchTag;

    @Column(nullable = false)
    private String storyText;

    @Column(nullable = false)
    private String projectDescript;

    @Column(nullable = false)
    private String openReservation;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private String makerType;

    @Column(nullable = false)
    private String progress;

    @Column(nullable = false)
    private String deliveryCheck;

    @Column
    private int deliveryPrice;

    @Column
    private LocalDateTime deliveryStartDate;

    @Column
    private int supportCount;

    @Column
    private int pickCount;

    @Column
    private int collectQuantity;

    @JsonIgnore
    @JoinColumn(name = "memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy = "fundingProject", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reward> rewards;

    @JsonIgnore
    @OneToMany(mappedBy = "fundingProject")
    private List<PickProject> pickProjects;

    @JsonIgnore
    @OneToMany(mappedBy = "fundingProject")
    private List<DoSupport> doSupports;
}
