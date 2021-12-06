package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import dto.BoardFile;

public class BoardFileDAOImpl implements BoardFileDAO{

	@Override
	public int insert(BoardFile boardfile) {
		try(SqlSession session = MBConn.getSession()){
			return session.insert("BoardFileMapper.insert",boardfile);
			}
	}

	@Override
	public int update(BoardFile boardfile) {
		try(SqlSession session = MBConn.getSession()){
			return session.update("BoardFileMapper.update",boardfile);
			}
	}

	@Override
	public int delete(int fnum) {
		try(SqlSession session = MBConn.getSession()){
			return session.delete("BoardFileMapper.delete",fnum);
			}
	}

	@Override
	public BoardFile selectOne(int fnum) {
		try(SqlSession session = MBConn.getSession()){
			return session.selectOne("BoardFileMapper.selectOne",fnum);
			}
	}

	@Override
	public List<BoardFile> selectList(int bnum) {
		try(SqlSession session = MBConn.getSession()){
			return session.selectList("BoardFileMapper.selectList",bnum);
			}
	}

	@Override
	public int delete_bnum(int bnum) {
		try (SqlSession session = MBConn.getSession()){
			return session.delete("BoardFileMapper.delete_bnum", bnum);
		}
	}



}
