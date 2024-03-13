package com.mapwithplan.mapplan.search.member.infrastructure;


import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.member.infrastructure.entity.MemberEntity;
import com.mapwithplan.mapplan.search.member.service.port.SearchMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class SearchMemberRepositoryImpl implements SearchMemberRepository {


    private final SearchMemberJPARepository searchMemberJPARepository;

    /**
     * 이메일이 완전히 일치하지 않아도 조회가 됩니다.
     * 입력받은 이메일의 앞부분의 일부를 기반으로 조회합니다.
     * @param email 이메일의 일부입니다.
     * @return 조회된 회원 엔티티를 return 합니다.
     */
    @Override
    public List<Member> findByEmailStartingWith(String email){
        List<MemberEntity> byEmail = searchMemberJPARepository.findByEmailStartingWith(email);
        return byEmail.stream()
                .map(MemberEntity::toModel)
                .toList();

    }
}
