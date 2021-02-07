package com.example.datajpa.controller;

import com.example.datajpa.dto.MemberDto;
import com.example.datajpa.entity.Member;
import com.example.datajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("{id}")
    public String findMember(@PathVariable Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 99; i++) {
            memberRepository.save(new Member("memeber" + i));
        }
    }

    @GetMapping
    public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) {

        Page<Member> page = memberRepository.findAll(pageable);
        // convert from Page<Member> to Page<MemberDto>
        return page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
    }
}
