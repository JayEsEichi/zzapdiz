package com.example.zzapdiz.fundingproject.response;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.reward.domain.Reward;
import com.example.zzapdiz.share.media.Media;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProjectReadResponseDto {
    private FundingProject fundingProject;
    private String remainDate;
    private String makerName;
    private List<Reward> rewards;
    private List<Media> medias;
    private Long countPick;
    private Long countSupport;
    private List<FundingProject> similarProjects;
    private List<FundingProject> makersOtherProjects;
}
