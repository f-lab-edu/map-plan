package com.mapwithplan.mapplan.mock.postmock;

import com.mapwithplan.mapplan.post.domain.Post;
import com.mapwithplan.mapplan.post.domain.PostImg;
import com.mapwithplan.mapplan.post.service.port.PostImgRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FakePostImgRepository implements PostImgRepository {

    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<PostImg> data = new ArrayList<>();
    @Override
    public List<PostImg> saveAll(List<PostImg> postImgList) {
        for (PostImg postImg : postImgList) {
            if(postImg.getId() ==null || postImg.getId() == 0){
                PostImg newPostImg = PostImg.builder()
                        .id(autoGeneratedId.incrementAndGet())
                        .storeFileName(postImg.getStoreFileName())
                        .uploadFileName(postImg.getUploadFileName())
                        .post(postImg.getPost())
                        .registrationDate(postImg.getRegistrationDate())
                        .build();
                data.add(newPostImg);
            } else{
                data.removeIf(test -> Objects.equals(test.getId(), postImg.getId()));
                data.add(postImg);
            }
        }
        return postImgList;
    }

    @Override
    public PostImg findByPostIdAndStoreFileName(Long postId, String storeFileName) {
        return null;
    }


}
