package com.example.datajpa.repository;

import com.example.datajpa.entity.Member;
import com.example.datajpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeamRepositoryTest {

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void basicCRUD() {
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        // findById() testing
        Team findTeam1 = teamRepository.findById(team1.getId()).get();
        Team findTeam2 = teamRepository.findById(team2.getId()).get();

        assertEquals(findTeam1, team1);
        assertEquals(findTeam2, team2);

        // findAll() testing
        List<Team> all = teamRepository.findAll();
        assertEquals(all.size(), 2);

        // count() testing
        long count = teamRepository.count();
        assertEquals(count, 2);

        // delete() testing
        teamRepository.delete(team1);
        teamRepository.delete(team2);

        assertEquals(teamRepository.count(), 0);
    }
}