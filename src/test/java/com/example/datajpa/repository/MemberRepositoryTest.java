package com.example.datajpa.repository;

import com.example.datajpa.dto.MemberDto;
import com.example.datajpa.entity.Member;
import com.example.datajpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Optional<Member> findMember = memberRepository.findById(savedMember.getId());

        if (findMember.isPresent()) {
            assertEquals(findMember.get().getId(), member.getId());
            assertEquals(findMember.get().getUsername(), member.getUsername());
        }
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertEquals(result.get(0).getUsername(), "AAA");
        assertEquals(result.get(0).getAge(), 20);
        assertEquals(result.size(), 1);
    }

    @Test
    public void testFindUser() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 15);

        assertEquals(result.get(0).getUsername(), "AAA");
        assertEquals(result.get(0).getAge(), 20);
        assertEquals(result.size(), 1);
    }

    @Test
    public void testFindUsernameList() {
        Member m1 = new Member("AAA1", 10);
        Member m2 = new Member("AAA2", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUsernameList();

        assertEquals(result.size(), 2);
        assertTrue(result.contains("AAA1"));
        assertTrue(result.contains("AAA2"));
    }

    @Test
    public void testFindMemberDto() {

        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA1", 10);
        m1.changeTeam(team);
        memberRepository.save(m1);

        List<MemberDto> result = memberRepository.findMemberDto();

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getUsername(), "AAA1");
        assertEquals(result.get(0).getTeamName(), "teamA");
    }

    @Test
    public void testFindByNames() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> usernameList = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        assertEquals(usernameList.size(), 2);
        assertEquals(usernameList.get(0).getUsername(), "AAA");
        assertEquals(usernameList.get(1).getUsername(), "BBB");
    }

    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> members = memberRepository.findByAge(age, pageRequest);

        // total page
        assertEquals(members.getTotalPages(), 2);

        // current page number
        assertEquals(members.getNumber(), 0);

        // total number of elements
        assertEquals(members.getTotalElements(), 6);

        // number of element in the current page
        assertEquals(members.getSize(), 3);

        // has next page?
        assertTrue(members.hasNext());

        // has previous page?
        assertFalse(members.hasPrevious());

        // first element
        assertEquals(members.getContent().get(0).getUsername(), "member6");

        // second element
        assertEquals(members.getContent().get(1).getUsername(), "member5");

        // third element
        assertEquals(members.getContent().get(2).getUsername(), "member4");
    }

    @Test
    public void bulk() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);
        assertEquals(resultCount, 3);

        List<Member> member5 = memberRepository.findByUsername("member5");
        assertEquals(member5.get(0).getAge(), 41);
    }
}