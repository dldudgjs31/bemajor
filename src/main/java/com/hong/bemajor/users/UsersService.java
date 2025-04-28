package com.hong.bemajor.users;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsersService {
    private final PasswordEncoder passwordEncoder;
    private final UsersDao usersDao;

    public UsersService(UsersDao usersDao, PasswordEncoder passwordEncoder) {
        this.usersDao = usersDao;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원 목록 조회
    public List<UsersDto> getAllUsers() {
        return usersDao.selectAllMembers();
    }

    // 회원 등록
    public void addUser(UsersDto member) {
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        usersDao.insertMember(member);
    }
}