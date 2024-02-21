package com.mapwithplan.mapplan.post.infrastructure.entity;

import com.mapwithplan.mapplan.common.timeutils.entity.BaseTimeEntity;
import com.mapwithplan.mapplan.member.infrastructure.entity.MemberEntity;
import com.mapwithplan.mapplan.post.domain.PostStatus;
import com.mapwithplan.mapplan.post.domain.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "post")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity extends BaseTimeEntity {

    @Column(name = "post_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity memberEntity;

    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "anonymousName")
    private String anonymousName;
    @Column(name = "count_like")
    private Integer countLike;
    @Column(name = "location")
    private String location;

    @Column(name = "post_status")
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @Builder
    public PostEntity(LocalDateTime createdAt, LocalDateTime modifiedAt, Long id, MemberEntity memberEntity, String title, String content, String anonymousName, Integer countLike, String location, PostStatus postStatus) {
        super(createdAt, modifiedAt);
        this.id = id;
        this.memberEntity = memberEntity;
        this.title = title;
        this.content = content;
        this.anonymousName = anonymousName;
        this.countLike = countLike;
        this.location = location;
        this.postStatus = postStatus;
    }

    public static PostEntity from(Post post){
        return PostEntity.builder()
                .id(post.getId())
                .memberEntity(MemberEntity.from(post.getMember()))
                .anonymousName(post.getAnonymousName())
                .title(post.getTitle())
                .content(post.getContent())
                .countLike(post.getCountLike())
                .postStatus(post.getPostStatus())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .location(post.getLocation())
                .build();
    }
    public Post toModel(){
        return Post.builder()
                .id(id)
                .member(memberEntity.toModel())
                .title(title)
                .content(content)
                .anonymousName(anonymousName)
                .countLike(countLike)
                .postStatus(postStatus)
                .createdAt(getCreatedAt())
                .modifiedAt(getModifiedAt())
                .location(location)
                .build();
    }



}
