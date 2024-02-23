package com.mapwithplan.mapplan.member.controller.port;

import com.mapwithplan.mapplan.member.domain.EditMember;
import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.domain.MemberCreate;

/**
 * 회원과 관련된 서비스 로직을 기록해두는 interface 입니다.
 * 이 인터페이스를 이용해 controller 에서 구현을 하면 됩니다.
 */
public interface MemberService {
    /**
     * 회원 생성에 대한 메서드 입니다.
     * @param memberCreate 회원 생성을 위한 dto 입니다. email, password, name, phone 필드를 가지고 있습니다.
     * @return Member 도메인을 반환합니다.
     */
    Member saveMember(MemberCreate memberCreate);

    /**
     * 회원가입 요청 -> 인증메일을 발송 -> 아래의 verifyEmail 메서드 호출
     * 위와 같은 방식으로 진행을하며 이메일 인증을 위한 메서드입니다.
     * @param id  controller 에서 @GetMapping("/{id}/verify") 를 위한 id 입니다.
     * @param certificationCode 인증 코드는 공통 유틸인 UuidHolder 를 통해 생성된 코드입니다.
     */
    void verifyEmail(long id, String certificationCode);

    /**
     * 회원 ID 를 통해 조회 하는 메서드 입니다.
     * @param id 회원 Id 입니다.
     * @return member 회원 도메인을 return 합니다.
     */
    Member findById(long id);

    /**
     * accessToken 을 parser 를 통해 정보를 찾아오는 메서드입니다.
     * @param accessToken 헤더에 있는 정보를 활용합니다.
     * @return
     */
    Member findByEmailUseAccessToken(String accessToken);

    /**
     * 회원을 조회한후 회원의 상태메세지, 번호를 변경합니다.
     * @param authorizationHeader 회원을 조회하는 것에 사용됩니다.
     * @param editMember
     * @return
     */
    Member editMemberDetail(String authorizationHeader, EditMember editMember);

}
