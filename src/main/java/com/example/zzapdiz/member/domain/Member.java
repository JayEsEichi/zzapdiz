package com.example.zzapdiz.member.domain;

import com.example.zzapdiz.dofundproject.domain.DoFund;
import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.pickproject.domain.PickProject;
import com.example.zzapdiz.share.Timestamped;
import com.example.zzapdiz.supportproject.domain.DoSupport;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Member extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private int point;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FundingProject> fundingProjects;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<PickProject> pickProjects;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<DoSupport> doSupports;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private DoFund doFund;

}
