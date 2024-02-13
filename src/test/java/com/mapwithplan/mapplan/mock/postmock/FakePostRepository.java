package com.mapwithplan.mapplan.mock.postmock;

import com.mapwithplan.mapplan.post.domain.Post;
import com.mapwithplan.mapplan.post.service.port.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class FakePostRepository implements PostRepository {
    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<Post> data = new ArrayList<>();

    @Override
    public Post createPost(Post post) {
        if(post.getId() ==null || post.getId() == 0){
            Post newPost = Post.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .title(post.getTitle())
                    .anonymousName(post.getAnonymousName())
                    .content(post.getContent())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .ePostStatus(post.getEPostStatus())
                    .countLike(post.getCountLike())
                    .location(post.getLocation())
                    .member(post.getMember())
                    .build();
            data.add(newPost);
            return newPost;
        } else{
            data.removeIf(test -> Objects.equals(test.getId(), post.getId()));
            data.add(post);
            return post;
        }
    }
}