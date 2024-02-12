package com.mapwithplan.mapplan.post.infrastructure;

import com.mapwithplan.mapplan.post.infrastructure.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJPARepository extends JpaRepository<PostEntity,Long> {
}
