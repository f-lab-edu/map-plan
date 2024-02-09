package com.mapwithplan.mapplan.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    private static String commonNotFoundResourceMessage(String datasource, String id) {
        return String.format("%s 에서 %s 를 찾을 수 없습니다.",datasource,id);
    }
    public ResourceNotFoundException(String datasource, long id) {
        super(commonNotFoundResourceMessage(datasource,Long.toString(id)));
    }

    public ResourceNotFoundException(String datasource, String id) {
        super(commonNotFoundResourceMessage(datasource,id));
    }
}
