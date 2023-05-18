package com.example.zzapdiz.store.domain;

import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.share.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class StoreProject extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long storeProjectId;

    @Column(nullable = false)
    private String projectTitle;

    @Column(nullable = false)
    private String projectCategory;

    @Column(nullable = false)
    private String projectType;

    @Column(nullable = false)
    private String adultCheck;

    @Column(nullable = false)
    private String searchTag;

    @Column(nullable = false)
    private String storyText;

    @Column(nullable = false)
    private String projectDescript;

    @Column(nullable = false)
    private String deliveryCheck;

    @Column(nullable = false)
    private int deliveryPrice;

    @Column(nullable = false)
    private Long fundingProjectId;

    @JsonIgnore
    @JoinColumn(name = "memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

}
