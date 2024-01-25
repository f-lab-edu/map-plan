package com.mapwithplan.mapplan.member.service.port;

public interface MailSender {

    void send(String email, String title, String content);
}
