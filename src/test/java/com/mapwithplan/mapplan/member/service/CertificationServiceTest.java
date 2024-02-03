package com.mapwithplan.mapplan.member.service;

import com.mapwithplan.mapplan.mock.FakeMailSender;
import com.mapwithplan.mapplan.mock.TestUuidHolder;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class CertificationServiceTest {


    @Test
    @DisplayName("인증 메일 발송 테스트 메일이 발송 되었을까?")
    void mailSendTest() {
        //Given

        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);
        TestUuidHolder testUuidHolder = new TestUuidHolder("test");
        String email = "test@gmail.com";


        //When
        //Then
        assertThatCode(()->certificationService.send(email, 1,testUuidHolder.random()))
                .doesNotThrowAnyException();
        assertThat(fakeMailSender.fakeMailSenderIsExecute).isEqualTo(true);
    }


}