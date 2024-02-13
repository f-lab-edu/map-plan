package com.mapwithplan.mapplan.post.infrastructure.entity;

import com.mapwithplan.mapplan.post.domain.PostImg;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Table(name = "post_img")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImgEntity {

    @Column(name = "post_img_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;

    @Column(name = "upload_file_name")
    private String uploadFileName;

    @Column(name = "store_file_name")
    private String storeFileName;

    @Column(name ="registration_date")
    @CreatedDate
    private LocalDateTime registrationDate;

    @Builder
    public PostImgEntity(Long id, PostEntity postEntity, String uploadFileName, String storeFileName, LocalDateTime registrationDate) {
        this.id = id;
        this.postEntity = postEntity;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.registrationDate = registrationDate;
    }

    public PostImg toModel(){
        return PostImg.builder()
                .id(id)
                .post(postEntity.toModel())
                .uploadFileName(uploadFileName)
                .storeFileName(storeFileName)
                .registrationDate(registrationDate)
                .build();
    }

    public static PostImgEntity from(PostImg postImg){
        return PostImgEntity.builder()
                .id(postImg.getId())
                .postEntity(PostEntity.from(postImg.getPost()))
                .uploadFileName(postImg.getUploadFileName())
                .storeFileName(postImg.getStoreFileName())
                .registrationDate(postImg.getRegistrationDate())
                .build();
    }

}
