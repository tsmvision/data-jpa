package com.example.datajpa.repository;

import com.example.datajpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

// jdbc, native jpa allowed

/**
 * Allow jdb, jpa, hibernate, QueryDSL, and etc
 */
@RequiredArgsConstructor
// custom implementation class name should be [repository name] + "impl"
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
