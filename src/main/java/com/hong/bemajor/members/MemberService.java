package com.hong.bemajor.members;

import org.springframework.dao.DataAccessException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao, PasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원 목록 조회
    public List<MemberDto> getAllMembers() {
        return memberDao.selectAllMembers();
    }

    // 회원 등록
    @Secured("ROLE_SYSOP")
    public void addMember(MemberDto member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberDao.insertMember(member);
    }
}