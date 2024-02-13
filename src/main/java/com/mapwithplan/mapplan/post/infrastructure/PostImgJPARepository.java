package com.mapwithplan.mapplan.post.infrastructure;

import com.mapwithplan.mapplan.post.infrastructure.entity.PostImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImgJPARepository extends JpaRepository<PostImgEntity, Long> {
}
