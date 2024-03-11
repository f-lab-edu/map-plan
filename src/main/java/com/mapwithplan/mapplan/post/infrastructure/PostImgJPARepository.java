package com.mapwithplan.mapplan.post.infrastructure;

import com.mapwithplan.mapplan.post.infrastructure.entity.PostImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostImgJPARepository extends JpaRepository<PostImgEntity, Long> {

    @Query("SELECT p FROM PostImgEntity p WHERE p.postEntity.id = :postId AND p.storeFileName = :storeFileName")
    Optional<PostImgEntity> findByPostIdAndStoreFileName(Long postId, String storeFileName);
}
