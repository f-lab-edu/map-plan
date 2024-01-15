package com.mapwithplan.dummyapi;

public class DummyApi {

    public static void main(String[] args) {

        System.out.println("new DummyApi().dummyApiTest(\"test Api 입니다.\"); = "
                + new DummyApi().dummyApiTest("test Api 입니다."));
    }

    public String dummyApiTest(String dummy){
        String plus = " (추가 내용입니다.)";
        StringBuilder stringBuilder = new StringBuilder(dummy);
        stringBuilder.append(plus);

        return stringBuilder.toString();
    }


}
