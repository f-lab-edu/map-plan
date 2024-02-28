package com.mapwithplan.mapplan.post.domain;



import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockProvider;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 이미지 파일의 메타데이터를 저장할 때 사용하는 도메인 입니다.
 */
@Getter
public class PostImg {

    private Long id;
    //업로드 당시 회원이 올린 파일명 입니다.
    private String uploadFileName;

    private Post post;

    //저장을 할때 변경한 파일명 입니다.
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
     * @param clockProvider
     * @return
     */
    public static PostImg from(Post post,String uploadFileName, String storeFileName, TimeClockProvider clockProvider){
        return PostImg.builder()
                .uploadFileName(uploadFileName)
                .post(post)
                .storeFileName(storeFileName)
                .registrationDate(clockProvider.clockProvider())
                .build();
    }

}
