package com.mapwithplan.mapplan.member.controller.port;


import com.mapwithplan.mapplan.member.controller.response.MemberCreateResponse;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.domain.MemberCreate;

public interface MemberService {

    Member saveMember(MemberCreate memberCreate);
    void verifyEmail(long id, String certificationCode);

    Member findById(long id);

}
