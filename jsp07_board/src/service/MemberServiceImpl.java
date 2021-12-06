package service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.MemberDAO;
import dao.MemberDAOImpl;
import dto.Member;

public class MemberServiceImpl implements MemberService{
	private MemberDAO mdao = new MemberDAOImpl();
	@Override
	public String insert(Member member) {
		String salt = saltmake(); //솔트생성
		String secretpw = sha256(member.getPasswd(), salt);
		member.setPasswd(secretpw);
		member.setSalt(salt); //솔트 db에 추가
		int cnt = mdao.insert(member);
		if(cnt>0) {
			return "추가 성공";
		}else {
			return "추가 실패";
		}
	}
	//salt를 랜덤하게 만들기
	public String saltmake() {
		String salt = null;
		try {
			//난수를생성해준다
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			byte[] bytes = new byte[16]; //빈배열
			sr.nextBytes(bytes); //랜덤한 값을 bytes에 만든다 
			//byte데이터를 String형으로 
			salt = new String(Base64.getEncoder().encode(bytes));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return salt;
	}
	
	//평문을 암호문으로 변경
	public String sha256(String passwd, String salt) {
		StringBuffer sb = new StringBuffer();
		try {
			//SHA-256 : 단방향 암호기법,복호화 불가능 256bit(16진수 64자리)
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(passwd.getBytes()); // 문자열을 바이트 배열로 변경해서 업데이트에게 전달
			md.update(salt.getBytes()); //솔트를 추가
			
			
			byte[] data = md.digest(); //암호화된 바이트 배열(32byte)
			//16진수 문자열로 변경, sb변수에 추가
			for(byte b : data) {
				sb.append(String.format("%02x", b));
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	@Override
	public String update(Member member , String changepw ) {
		//한건조회
		String msg = null;
		Member dbmember = mdao.selectOne(member.getEmail());
		String dbpw = dbmember.getPasswd(); //암호화된 db비빌번호
		//입력한 비밀번호 암호화(권한체크)
		String secretpw = sha256(member.getPasswd(), dbmember.getSalt());
		
		if (dbpw.equals(secretpw)) {
			//비밀번호가 변경됬다면 
			if (!changepw.equals("")) {
				//솔트
				String salt = saltmake();
				//새로운 암호비밀번호를 얻기위해서
				secretpw =sha256(changepw, salt);
				member.setPasswd(secretpw);
				member.setSalt(salt);
			}else { //비밀번호가 변경이 안됬다면 기존정보 다시 세팅
				member.setPasswd(dbpw);
				member.setSalt(dbmember.getSalt());
				
			}
			System.out.println("service:" + member);
			mdao.update(member);
			msg = "변경 완료";
		}else {
			msg = "비밀번호 불일치";
		}
		return msg;
	}

	@Override
	public String delete(String email) {
		int cnt = mdao.delete(email);
		if(cnt>0) {
			return "삭제 성공";
		}else {
			return "삭제 실패";
		}
	}

	@Override
	public Member selectOne(String email) {
		return mdao.selectOne(email);
	}

	@Override
	public List<Member> selectList(String findkey, String findvalue) {
		Map<String,String> map = new HashMap<>();
		map.put("findkey", findkey);
		map.put("findvalue", findvalue);
		return mdao.selectList(map);
	}
	@Override
	public Map<String, String> login(String email, String passwd) {
		Map<String, String> map = new HashMap<>();
		//0: 로그인 성공
		//1: 이메일 불일치
		//2: 패스워드 불일치
		String msg = null; //메세지
		String rcode = null; //결과 코드값
		//한건조회
		Member member = mdao.selectOne(email);
		if (member == null) {
			msg = "존재하지 않는 이메일입니다.";
			rcode = "1";
		}else{
			String dbpw = member.getPasswd();
			String salt = member.getSalt();
			
			//암호화된 비밀번호 생성
			String secretpw = sha256(passwd,salt);
			if (dbpw.equals(secretpw)) {
				msg = "로그인 성공";
				rcode = "0";
			}else {
				msg = "비밀번호가 일치하지 않습니다";
				rcode = "2";
			}
			
		}
		map.put("msg", msg);
		map.put("rcode", rcode);
		System.out.println(map);
		return map;
	}

}
