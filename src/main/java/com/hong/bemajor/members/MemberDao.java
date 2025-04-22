package com.hong.bemajor.members;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface MemberDao {
    public int insertMember(MemberDto vo);
    public List<MemberDto> selectAllMembers();
}
