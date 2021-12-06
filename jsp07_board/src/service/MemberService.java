package service;

import java.util.List;
import java.util.Map;

import dto.Member;

public interface MemberService {
	String insert(Member member);
	String update(Member member, String changepw);
	String delete(String email);
	Member selectOne(String email);
	List<Member> selectList(String findkey,String findvalue);
	Map<String, String> login(String email, String passwd);
}
