package com.mapwithplan.mapplan.post.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DownloadPostFile {

    private String uploadFileName;
    private String storeFileName;

    private String fullPath;


    @Builder
    public DownloadPostFile(String uploadFileName, String storeFileName, String fullPath) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.fullPath = fullPath;
    }

    public static DownloadPostFile from(String uploadFileName, String storeFileName, String fullPath){
        return DownloadPostFile.builder()
                .fullPath(fullPath)
                .storeFileName(storeFileName)
                .uploadFileName(uploadFileName)
                .build();
    }
}
