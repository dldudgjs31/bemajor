package com.hong.bemajor.members;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    // 회원 목록 조회
    public List<MemberDto> getAllMembers() {
        return memberDao.selectAllMembers();
    }

    // 회원 등록
    public void addMember(MemberDto member) {
        memberDao.insertMember(member);
    }
}