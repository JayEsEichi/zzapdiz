package com.example.zzapdiz.share.media;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Media {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long mediaId;

    @Column(nullable = false)
    private String mediaRealName;

    @Column(nullable = false)
    private String mediaUidName;

    @Column(nullable = false)
    private String mediaType;

    @Column
    private String mediaUrl;

    @Column(nullable = false)
    private String mediaPurpose;

    @Column
    private Long fundingProjectId;

//    @JoinColumn(name = "fundingProjectId", nullable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
//    private FundingProject fundingProject;
}
