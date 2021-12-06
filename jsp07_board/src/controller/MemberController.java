package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import dto.Member;
import service.MemberService;
import service.MemberServiceImpl;

/**
 * Servlet implementation class MemberController
 */
@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private MemberService mservice = new MemberServiceImpl();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		System.out.println(uri);
		String path = request.getContextPath();
  	   	request.setCharacterEncoding("utf-8");
		if(uri.contains("add")) {
			//회원 등록
			//saveDirectory: 파일을 저장할 경로(서버)
			//String saveDirectory = "D:/Study/savedir";
			//web.xml에서 저장 경로 읽기
			String saveDirectory = getServletContext().getInitParameter("savedir");
			int size = 1024 * 1024 * 10;//10mb : 업로드할 파일 사이즈
			//new DefaultFileRenamePolicy() : 같은 이름의 파일이 있을 때, 파일이름 변경해줌
			MultipartRequest multi = new MultipartRequest(request, saveDirectory, size,"utf-8", new DefaultFileRenamePolicy());
			//MultipartRequest 객체를 이용해서 데이터 가져옴
			String email = multi.getParameter("email");
			String passwd = multi.getParameter("passwd");
			String zipcode = multi.getParameter("zipcode");
			String addr = multi.getParameter("addr");
			String addrdetail = multi.getParameter("addrdetail");
			//실제 저장된 파일 이름 가져오기
			String file = multi.getFilesystemName("file");
			if (file==null) file=""; //파일이 없을 때
			//객체 생성
			Member member = new Member();
			member.setEmail(email);
			member.setPasswd(passwd);
			member.setZipcode(zipcode);
			member.setAddr(addr);
			member.setAddrdetail(addrdetail);
			member.setFilename(file);
			//저장
			String msg = mservice.insert(member);
			System.out.println(msg);
			//자동 로그인 처리
			response.sendRedirect(path + "/views/home.jsp?msg=" + URLEncoder.encode(msg, "utf-8"));
			//response.sendRedirect(path+"/member/login?email="+email+"&passwd"+passwd);
			
		}else if (uri.contains("login")) {
			//로그인
			String email = request.getParameter("email");
			String passwd = request.getParameter("passwd");
			System.out.println(email);
			System.out.println(passwd);
			
			Map<String, String> map = mservice.login(email,passwd);
			String rcode = map.get("rcode");
			String msg = map.get("msg");
			if(rcode.equals("0")) {
				HttpSession session = request.getSession();
				session.setAttribute("email",email );
				session.setMaxInactiveInterval(60*60*5); //5시간
				
				String idsave = request.getParameter("idsave");
				Cookie email_cookie = new Cookie("email", email);
				email_cookie.setPath(path);
				if(idsave != null) {	
					email_cookie.setMaxAge(60*60*24); //하루
					}else if (idsave == null) {
					email_cookie.setMaxAge(0);
					}
				response.addCookie(email_cookie);
				
				response.sendRedirect(path + "/views/home.jsp?msg=" + URLEncoder.encode(msg, "utf-8"));	
			}else if (rcode.equals("1") || rcode.equals("2")) {
				response.sendRedirect(path + "/views/member/login.jsp?msg=" + URLEncoder.encode(msg, "utf-8"));	
			}
			
		}else if (uri.contains("logout")) {
			//로그아웃
			HttpSession session = request.getSession();
			session.invalidate();
			String msg = "로그아웃 되었습니다.";
			response.sendRedirect(path + "/views/home.jsp?msg=" + URLEncoder.encode(msg, "utf-8"));	
			
		}else if (uri.contains("myinfo")) {
			//내정보
			//이메일을 세션에서 가져오기
			HttpSession session = request.getSession();
			String email = (String)session.getAttribute("email");
			Member member = mservice.selectOne(email);
			//forward 방식으로 myinfo.jsp 이동
			request.setAttribute("member", member);
			request.getRequestDispatcher("/views/member/myinfo.jsp")
					.forward(request, response);
		}else if (uri.contains("modify")) {
			//회원 수정
			//saveDirectory: 파일을 저장할 경로(서버)
			String saveDirectory = getServletContext().getInitParameter("savedir");
			int size = 1024 * 1024 * 10;//10mb : 업로드할 파일 사이즈
			//new DefaultFileRenamePolicy() : 같은 이름의 파일이 있을 때, 파일이름 변경해줌
			MultipartRequest multi = new MultipartRequest(request, saveDirectory, size,"utf-8", new DefaultFileRenamePolicy());
			//MultipartRequest 객체를 이용해서 데이터 가져옴
			String email = multi.getParameter("email");
			String passwd = multi.getParameter("passwd");
			String changepw = multi.getParameter("changepw");
			String zipcode = multi.getParameter("zipcode");
			String addr = multi.getParameter("addr");
			String addrdetail = multi.getParameter("addrdetail");
			String filename = multi.getParameter("filename"); //파일이름
			String filedel = multi.getParameter("filedel");
			//실제 저장된 파일 이름 가져오기
			String newfilename = multi.getFilesystemName("file"); //파일
			if (newfilename!=null) 
				filename=newfilename;
			else if(filedel != null)
				filename = "";
			//객체 생성
			Member member = new Member();
			member.setEmail(email);
			member.setPasswd(passwd);
			member.setZipcode(zipcode);
			member.setAddr(addr);
			member.setAddrdetail(addrdetail);
			member.setFilename(filename);

			String msg = mservice.update(member, changepw);

			//
			response.sendRedirect(path+"/member/myinfo?msg=" + URLEncoder.encode(msg, "utf-8"));	
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
