package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import dto.BoardFile;

class JunitFileTest {
	BoardFileDAO bfdao = new BoardFileDAOImpl();
	@Test
	void testInsert() {
		BoardFile bf = new BoardFile();
		bf.setBnum(1);
		bf.setFilename("kace.png");
		int cnt = bfdao.insert(bf);
		System.out.println(cnt+"건 추가");
	}
	@Test
	void testUpdate() {
		BoardFile bf = new BoardFile();
		bf.setFilename("jace.jpg");
		bf.setFnum(2);
		int cnt = bfdao.update(bf);
		System.out.println(cnt+"건 수정");
	}
	@Test
	void testDelete() {
		int cnt = bfdao.delete(3);
		System.out.println(cnt+"건 삭제");
	}
	@Test
	void testselectOne() {
		BoardFile bf = bfdao.selectOne(1);
		System.out.println(bf);
	}
	@Test
	void testselectList() {
		List<BoardFile> bflist = bfdao.selectList(1);
		System.out.println(bflist);
	}

}
