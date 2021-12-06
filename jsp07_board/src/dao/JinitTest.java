package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import dto.Board;
import dto.Page;

class JinitTest {
	BoardDAO bdao = new BoardDAOImpl();
	@Test
	void testMBConn() {
		MBConn.getSession();
	}
	@Test
	void testInsert() {
		Board board = new Board();
		board.setEmail("fanakim@email.com");
		board.setSubject("제목");
		board.setContent("내용");
		board.setIp("192.168.0.1");
		int cnt = bdao.insert(board);
		System.out.println(cnt+"건 등록");
	}
	@Test
	void testUpdate() {
		Board board = new Board();
		board.setEmail("fanakim@email.com");
		board.setSubject("주파수");
		board.setContent("내용내용내용");
		board.setIp("192.168.0.100");
		board.setBnum(1);
		int cnt = bdao.update(board);
		System.out.println(cnt+"건 수정");
	}
	@Test
	void testDelete() {
		int cnt = bdao.delete(1);
		System.out.println(cnt+"건 삭제");
	}
	@Test
	void testSelctOne() {
		Board board = bdao.selectOne(1);
		System.out.println(board);
		assertNotEquals(null, board);
	}
	@Test
	void testSelectList() {
		Page page = new Page();
		page.setFindkey("email");
		page.setFindvalue("tig");
		List<Board> blist = bdao.selectList(page);
		System.out.println(blist);
	}
}
