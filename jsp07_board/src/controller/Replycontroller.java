package controller;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.Board;
import service.BoardService;
import service.BoardServiceImpl;
import service.ReplyService;
import service.ReplyServiceImpl;

/**
 * Servlet implementation class Replycontroller
 */
@WebServlet("/reply/*")
public class Replycontroller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ReplyService rservice = new ReplyServiceImpl();
	private BoardService bservice = new BoardServiceImpl();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("utf-8");
    	String uri = request.getRequestURI();
    	System.out.println(uri);
    	String path = request.getContextPath();
    	if (uri.contains("add")) {
			//댓글 등록
			String email = request.getParameter("email");
			String subject = request.getParameter("subject");
			String content = request.getParameter("content");
			int ref = Integer.parseInt(request.getParameter("ref"));
			int restep = Integer.parseInt(request.getParameter("restep"));
			int relevel = Integer.parseInt(request.getParameter("relevel"));
			Board board = new Board();
			board.setEmail(email);
			board.setSubject(subject);
			board.setContent(content);
			board.setIp(request.getRemoteAddr());
			//부모의 정보
			board.setRef(ref);
			board.setRestep(restep);
			board.setRelevel(relevel);
			System.out.println(board);
			
			String msg = rservice.insert(board);
			
			//redirect이동, /board/detail
			//원본의 bnum의 값은 ref(그룹번호)의 값과 같다
			response.sendRedirect(path+"/board/detail?bnum="+ref+"&msg="+URLEncoder.encode(msg,"utf-8"));
			
    	}else if(uri.contains("remove")) {
    		int bnum = Integer.parseInt(request.getParameter("bnum"));
    		int rnum = Integer.parseInt(request.getParameter("rnum")); //댓글
			String msg = bservice.delete(rnum);
			
			//redirect이동, /board/detail
			//원본의 bnum의 값은 ref(그룹번호)의 값과 같다
			response.sendRedirect(path+"/board/detail?bnum="+bnum+"&msg"+URLEncoder.encode(msg,"utf-8"));
    	}else if(uri.contains("modiform")) {
    		//댓글 수정폼으로
    		int bnum = Integer.parseInt(request.getParameter("bnum"));
    		Board board = rservice.selectOne(bnum);
    		System.out.println("controller: "+board);
    		request.setAttribute("board", board);
    		request.getRequestDispatcher("/views/board/replymodify.jsp")
    				.forward(request, response);
    		
    	}else if(uri.contains("modify")) {
    		int bnum = Integer.parseInt(request.getParameter("bnum")); //댓글의 번호
    		String email = request.getParameter("email");
    		String subject = request.getParameter("subject");
    		String content = request.getParameter("content");
    		Board board = new Board();
    		board.setBnum(bnum);
    		board.setEmail(email);
    		board.setSubject(subject);
    		board.setContent(content);
    		board.setIp(request.getRemoteAddr());
    		System.out.println("modifycon"+board);
    		String msg = rservice.update(board);
    		int ref = Integer.parseInt(request.getParameter("ref"));
    		
    		response.sendRedirect(
					path+"/board/detail?bnum="+ref+"&msg="+ URLEncoder.encode(msg, "utf-8"));
    	}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
