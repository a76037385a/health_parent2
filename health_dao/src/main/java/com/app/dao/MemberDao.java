package com.app.dao;

import com.app01.pojo.Member;

public interface MemberDao {

    Member findMemberByPhoneNumber(String phoneNumber);

    void addMember(Member member);

    Integer countRegisteByToday(String date);

    Integer countAllmember();

    Integer countThisWeekNewMember(String weekFirstDay);

    Integer countThisMonthNewMember(String monthFirstDay);

}
