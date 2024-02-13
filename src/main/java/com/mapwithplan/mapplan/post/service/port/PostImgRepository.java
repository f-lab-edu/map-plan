package com.mapwithplan.mapplan.post.service.port;

import com.mapwithplan.mapplan.post.domain.PostImg;

import java.util.List;

public interface PostImgRepository {
    /**
     * 파일의 메타 데이터를 저장할 때 사용하는 메서드 입니다.
     * @param postImgList
     * @return
     */
    List<PostImg>  saveAll(List<PostImg> postImgList);
}
