package com.mapwithplan.mapplan.member.service;

import com.mapwithplan.mapplan.member.service.port.MailSender;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 회원 인증과 관련된 클래스입니다.
 */

@Builder
@Service
@RequiredArgsConstructor
public class CertificationService {

    private final MailSender mailSender;

    /**
     * 메일을 발송하는 메서드 입니다.
     * @param email 발송하고자 하는 회원의 이메일입니다.
     * @param userId 회원 아이디
     * @param certificationCode UUID 인증 코드
     */
    public void send(String email, long userId, String certificationCode) {
        String certificationUrl = generateCertificationUrl(userId, certificationCode);
        String title = "Please certify your email address";
        String content = "Please click the following link to certify your email address: " + certificationUrl;
        mailSender.send(email, title, content);
    }
    //내부 메서드로 인증 Url 을 생성합니다.
    private String generateCertificationUrl(long userId, String certificationCode) {
        return "http://localhost:8080/member/" + userId + "/verify?certificationCode=" + certificationCode;
    }
}