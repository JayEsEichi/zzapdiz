package com.example.zzapdiz.exception.supportproject;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public interface DoSupportExceptionInterface {

    /** 본인이 생성한 프로젝트를 지지한 것인지 확인 **/
    Boolean supportMyProjectCheck(Member authMember, FundingProject project);
}
