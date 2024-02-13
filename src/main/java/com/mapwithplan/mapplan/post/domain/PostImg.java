package com.mapwithplan.mapplan.post.domain;


import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 이미지 파일의 메타데이터를 저장할 때 사용하는 도메인 입니다.
 */
@Getter
public class PostImg {

    private Long id;

    private String uploadFileName;

    private Post post;

    private String storeFileName;

    private LocalDateTime registrationDate;

    @Builder
    public PostImg(Long id, String uploadFileName,Post post, String storeFileName, LocalDateTime registrationDate) {
        this.id = id;
        this.uploadFileName = uploadFileName;
        this.post = post;
        this.storeFileName = storeFileName;
        this.registrationDate = registrationDate;
    }

    /**
     * 게시글의 이미지에 대한 정보를 담습니다.
     * @param post
     * @param uploadFileName
     * @param storeFileName
     * @param clockHolder
     * @return
     */
    public static PostImg from(Post post,String uploadFileName, String storeFileName, TimeClockHolder clockHolder){
        return PostImg.builder()
                .uploadFileName(uploadFileName)
                .post(post)
                .storeFileName(storeFileName)
                .registrationDate(clockHolder.clockHold())
                .build();
    }

}
