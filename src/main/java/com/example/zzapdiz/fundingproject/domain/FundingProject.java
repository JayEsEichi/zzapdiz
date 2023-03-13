package com.example.zzapdiz.fundingproject.domain;

import com.example.zzapdiz.share.MakerType;
import com.example.zzapdiz.share.ProjectCategory;
import com.example.zzapdiz.share.ProjectType;
import com.example.zzapdiz.share.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class FundingProject extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long fundingProjectId;

    @Column(nullable = false)
    private String projectTitle;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectCategory projectCategory;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectType projectType;

    @Column(nullable = false)
    private int achievedAmount;

    @Column(nullable = false)
    private String adultCheck;

    @Column(nullable = false)
    private String searchTag;

    @Column(nullable = false)
    private String storyText;

    @Column
    private String storyImage;

    @Column
    private String storyVideo;

    @Column(nullable = false)
    private String projectDescript;

    @Column(nullable = false)
    private String openReservation;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String thumbnailImage;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MakerType makerType;

    @Column(nullable = false)
    private String progress;

    @Column(nullable = false)
    private String deliveryCheck;

    @Column
    private String deliveryPrice;

    @Column
    private LocalDate deliveryStartDate;

}
