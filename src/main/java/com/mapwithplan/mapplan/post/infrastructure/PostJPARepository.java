package com.mapwithplan.mapplan.post.infrastructure;

import com.mapwithplan.mapplan.post.infrastructure.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostJPARepository extends JpaRepository<PostEntity,Long> {


    @Query("SELECT p FROM PostEntity p JOIN FETCH p.postImgEntityList WHERE p.id = :postId")
    PostEntity findPostWithImagesById(Long postId);

}
