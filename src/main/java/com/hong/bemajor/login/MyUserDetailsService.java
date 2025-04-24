package com.hong.bemajor.login;

import com.hong.bemajor.members.MemberDao;
import com.hong.bemajor.members.MemberDto;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security UserDetailsService 구현체
 * - 로그인 시 사용자 정보를 DB에서 조회하여 UserDetails 반환
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final MemberDao memberDao;

    /**
     * 생성자 주입
     * @param memberDao 사용자 조회 DAO
     */
    public MyUserDetailsService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    /**
     * 사용자명(username) 기반으로 UserDetails 조회
     * @param username 로그인 시 사용된 login_id
     * @return 인증에 사용될 UserDetails 객체
     * @throws UsernameNotFoundException 해당 사용자 없을 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        // DB에서 login_id로 사용자 정보 조회
        MemberDto member = memberDao.selectMemberByLoginId(username);

        // 사용자 정보가 없으면 예외 처리
        if (member == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        // Spring Security용 UserDetails 객체 생성
        return User.builder()
                .username(member.getLogin_id())          // 로그인 ID
                .password(member.getPassword())          // 암호화된 비밀번호
                .roles(member.getRank_id())              // 사용자 권한(역할) 설정
                .build();                                // UserDetails 반환
    }
}
