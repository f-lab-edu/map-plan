package com.mapwithplan.mapplan.common.exception;

public class DuplicateResourceException extends RuntimeException {


    public DuplicateResourceException(String email) {
        super(String.format("중복된 %s 입니다.",email));
    }
}
