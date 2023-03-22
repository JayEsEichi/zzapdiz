package com.example.zzapdiz.exception.pickproject;

import com.example.zzapdiz.fundingproject.domain.FundingProject;
import com.example.zzapdiz.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public interface PickProjectExceptionInterface {

    /** 본인이 생성한 프로젝트는 찜할 수 없다. **/
    Boolean pickMyProjectCheck(Member authMember, FundingProject project);

}
