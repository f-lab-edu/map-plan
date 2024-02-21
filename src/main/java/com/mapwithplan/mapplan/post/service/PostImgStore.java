package com.mapwithplan.mapplan.post.service;


import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockProvider;
import com.mapwithplan.mapplan.common.uuidutils.service.port.UuidHolder;
import com.mapwithplan.mapplan.post.domain.Post;
import com.mapwithplan.mapplan.post.domain.PostImg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시글에 사용되는 이미지를 저장하는 클래스 입니다.
 */

@Slf4j
@Component
public class PostImgStore {
    /**
     * 저장 경로를 지정해주는 필드 입니다.
     */
    @Value("${file.dir}")
    private String fileDir;

    /**
     * 저장 경로 + 파일 이름으로 완전한 경로를 return 해줍니다.
     * @param filename
     * @return
     */
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    /**
     * 저장 요청 받은 파일들을 처리하는 메서드 입니다.
     * 예외를 처리하는 로직을 추가했습니다.
     * @param postImgFiles 저장 요청이 들어온 이미지 파일들 입니다.
     * @param post 게시글에 대한 인스턴스 입니다. DB 에 저장하기 위한 용도 입니다.
     * @param clockHolder 생성 시간에 필요한 파라미터 입니다.
     * @param uuidHolder DB 저장에 사용되는 UUID 생성 클래스 입니다. DB 에는 저장용 이름과 처음에 Input 된 이름이 분류 됩니다.
     * @return
     */
    public List<PostImg> storeFiles(List<MultipartFile>  postImgFiles, Post post, TimeClockProvider clockHolder , UuidHolder uuidHolder)  {
        List<PostImg> storeFileResult = new ArrayList<>();
        try {
            for (MultipartFile multipartFile : postImgFiles) {
                if (!multipartFile.isEmpty()) {
                    storeFileResult.add(storeFile(multipartFile,post, clockHolder, uuidHolder ));
                }
            }
        } catch (IOException e){
            log.info("{}",e.getMessage());
        }
        return storeFileResult;
    }

    /**
     * 한개의 요청 파일을 저정합니다.
     * @param multipartFile 저장 요청이 들어온 이미지 파일들 입니다.
     * @param post 게시글에 대한 인스턴스 입니다. DB 에 저장하기 위한 용도 입니다.
     * @param clockHolder 생성 시간에 필요한 파라미터 입니다.
     * @param uuidHolder DB 저장에 사용되는 UUID 생성 클래스 입니다. DB 에는 저장용 이름과 처음에 Input 된 이름이 분류 됩니다.
     * @return
     * @throws IOException
     */
    public PostImg storeFile(MultipartFile multipartFile, Post post, TimeClockProvider clockHolder,UuidHolder uuidHolder) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename, uuidHolder);

        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return PostImg.from(post, originalFilename, storeFileName,clockHolder);
    }

    /**
     * 저장에 필요한 이름을 생성합니다. UUID 와 확장자를 같이 사용해서 생성합니다.
     * @param originalFilename
     * @param uuidHolder
     * @return
     */
    private String createStoreFileName(String originalFilename,UuidHolder uuidHolder) {
        String ext = extractExt(originalFilename);
        return uuidHolder.random() + "." + ext;
    }

    /**
     * 원래 이름에서 파일 뒤에 붙는 확장자의 이름을 분리합니다.
     * @param originalFilename 원래 파일의 이름입니다.
     * @return
     */
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
