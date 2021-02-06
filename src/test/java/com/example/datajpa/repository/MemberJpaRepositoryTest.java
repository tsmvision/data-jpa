package com.example.datajpa.repository;

import com.example.datajpa.entity.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(), member.getUsername());
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // findById() testing
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertEquals(findMember1, member1);
        assertEquals(findMember2, member2);

        // findAll() testing
        List<Member> all = memberJpaRepository.findAll();
        assertEquals(all.size(), 2);

        // count() testing
        long count = memberJpaRepository.count();
        assertEquals(count, 2);

        // delete() testing
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        assertEquals(memberJpaRepository.count(), 0);


    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertEquals(result.get(0).getUsername(), "AAA");
        assertEquals(result.get(0).getAge(), 20);
        assertEquals(result.size(), 1);
    }

    @Test
    public void paging() {
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));
        memberJpaRepository.save(new Member("member6", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        // when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);

        assertEquals(members.size(), 3);
        assertEquals(members.get(0).getUsername(), "member6");
        assertEquals(members.get(1).getUsername(), "member5");
        assertEquals(members.get(2).getUsername(), "member4");
    }
}