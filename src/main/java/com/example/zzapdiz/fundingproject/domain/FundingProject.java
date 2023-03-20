package com.example.zzapdiz.fundingproject.domain;

import com.example.zzapdiz.reward.domain.Reward;
import com.example.zzapdiz.share.media.Media;
import com.example.zzapdiz.share.project.MakerType;
import com.example.zzapdiz.share.project.ProjectCategory;
import com.example.zzapdiz.share.project.ProjectType;
import com.example.zzapdiz.share.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
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

//    @Column
//    private String storyImage;
//
//    @Column
//    private String storyVideo;

    @Column(nullable = false)
    private String projectDescript;

    @Column(nullable = false)
    private String openReservation;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

//    @Column(nullable = false)
//    private String thumbnailImage;

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

    @JsonIgnore
    @OneToMany(mappedBy = "fundingProject", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reward> rewards;

//    @OneToMany(mappedBy = "fundingProject", fetch = FetchType.LAZY,cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Media> medias;

}
