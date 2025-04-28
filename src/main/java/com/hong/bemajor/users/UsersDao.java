package com.hong.bemajor.users;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UsersDao {
    public int insertMember(UsersDto vo);
    public List<UsersDto> selectAllMembers();
    public UsersDto selectUserByLoginId(String login_id);
}
