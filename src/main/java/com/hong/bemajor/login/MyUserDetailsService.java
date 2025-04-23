package com.hong.bemajor.login;

import com.hong.bemajor.members.MemberDao;
import com.hong.bemajor.members.MemberDto;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MemberDao memberDao;

    public MyUserDetailsService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {


        // DB에서 사용자 정보를 조회
        MemberDto member = memberDao.selectMemberByLoginId(username);

        // 사용자 정보가 없으면 예외를 던짐
        if (member == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        // DB에서 조회한 사용자 정보를 바탕으로 UserDetails 객체 생성
        return User.builder()
                .username(member.getLogin_id())
                .password(member.getPassword())
                .roles("USER")// 비밀번호는 인코딩되어 있을 수 있으므로, 필요시 암호화 확인
                .build();
    }
}