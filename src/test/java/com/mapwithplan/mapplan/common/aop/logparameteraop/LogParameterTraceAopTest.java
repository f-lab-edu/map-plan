package com.mapwithplan.mapplan.common.aop.logparameteraop;

import com.mapwithplan.mapplan.common.aop.logparameteraop.annotation.LogInputTrace;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@SpringBootTest
class LogParameterTraceAopTest {

    @Autowired
    TestNameService testNameClass;
    @Test
    void testAop() {
        //Given
        List<String> test = new ArrayList<>();
        for (int i = 0; i < 10 ; i++) {
            test.add("test" + i);
        }
        TestDomain testDomain = new TestDomain(1L, "안녕하세요", test);
        ArrayList<TestDomain> testArray = new ArrayList<>();

        for (int i = 0; i < 10 ; i++) {
            TestDomain test123 = new TestDomain(1L, "이건 두번째 파라미터 값입니다."+i, test);
            testArray.add(test123);
        }
        //When
        testNameClass.testMethod(testDomain,testArray);
        //Then
        
    }
    @TestConfiguration
    static class TestConfig{
        @Bean
        public TestNameService testNameClass(){
            return new TestNameService();
        }
    }



    static class TestNameService{

        @LogInputTrace
        public void testMethod(TestDomain testDomain, List<TestDomain> testDomains){
            log.info("__________________________________________________________");
        }
    }

    static class TestDomain{
        private Long test;
        private String testString;

        private List<String> helloTestArray;

        public TestDomain(Long test, String testString, List<String> helloTestArray) {
            this.test = test;
            this.testString = testString;
            this.helloTestArray = helloTestArray;
        }

        @Override
        public String toString() {
            return "TestDomain{" +
                    "test=" + test +
                    ", testString='" + testString + '\'' +
                    ", helloTestArray=" + helloTestArray +
                    '}';
        }
    }

}