package com.example.zzapdiz.member.service;

import com.example.zzapdiz.exception.member.MemberExceptionInterface;
import com.example.zzapdiz.member.domain.Member;
import com.example.zzapdiz.share.DynamicQueryDsl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberExceptionInterface memberExceptionInterface;
    private final DynamicQueryDsl dynamicQueryDsl;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        memberExceptionInterface.checkEmail(email);
        Member authMember = dynamicQueryDsl.findMemberByEmail(email);

        return createUserDetails(authMember);
    }

    private UserDetails createUserDetails(Member member) {

        log.info("createUserDetails 실행");

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRoles().toArray(new String[0]))
                .build();
    }
}
