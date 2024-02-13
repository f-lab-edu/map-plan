package com.mapwithplan.mapplan.post.infrastructure;

import com.mapwithplan.mapplan.post.domain.PostImg;
import com.mapwithplan.mapplan.post.infrastructure.entity.PostImgEntity;
import com.mapwithplan.mapplan.post.service.port.PostImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 요청이 들어온 파일 메타 데이터를 저장할 때 사용하는 Repository 입니다.
 */
@RequiredArgsConstructor
@Repository
public class PostImgRepositoryImpl implements PostImgRepository {

    private final PostImgJPARepository postImgJPARepository;

    /**
     * 모든 도메인을 엔티티로 변경후, 저장합니다.
     * @param postImgList
     * @return
     */
    @Override
    public List<PostImg> saveAll(List<PostImg> postImgList) {
        List<PostImgEntity> postImgEntities = postImgList
                .stream()
                .map(PostImgEntity::from)
                .toList();
        return postImgJPARepository.saveAll(postImgEntities)
                .stream()
                .map(PostImgEntity::toModel)
                .toList();

    }
}
