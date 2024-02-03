package com.mapwithplan.mapplan.mock;

import com.mapwithplan.mapplan.member.service.port.MailSender;

public class FakeMailSender implements MailSender {
    public String email;
    public String title;
    public String content;
    public boolean fakeMailSenderIsExecute;
    @Override
    public void send(String email, String title, String content) {
        this.email = email;
        this.title = title;
        this.content = content;
        this.fakeMailSenderIsExecute = true;
    }
}
