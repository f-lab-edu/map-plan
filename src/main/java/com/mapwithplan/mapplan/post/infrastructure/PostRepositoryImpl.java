package com.mapwithplan.mapplan.post.infrastructure;


import com.mapwithplan.mapplan.post.domain.Post;
import com.mapwithplan.mapplan.post.infrastructure.entity.PostEntity;
import com.mapwithplan.mapplan.post.service.port.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepository {

    private final PostJPARepository postJPARepository;

    /**
     * JPA 를 활용해 생성된 post 도메인을 PostEntity 변경후 저장합니다.
     * @param post 생성된 게시글을 저장합니다.
     * @return .toModel()를 사용해 엔티티가 아닌 도메인으로 리턴합니다.
     */
    @Override
    public Post createPost(Post post) {
        return postJPARepository.save(PostEntity.from(post)).toModel();
    }

    @Override
    public Post findPostWithImagesById(Long postId) {
        return postJPARepository.findPostWithImagesById(postId).toModel();
    }
}
