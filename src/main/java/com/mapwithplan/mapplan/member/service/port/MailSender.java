package com.mapwithplan.mapplan.member.service.port;

/**
 * 메일을 보내는 interface 입니다.
 */
public interface MailSender {

    void send(String email, String title, String content);
}
