package com.atguigu.dao;

import com.atguigu.pojo.Member;

public interface MemberDao {

    Member findMemberByTelephone(String telephone);

    void add(Member member);
}
