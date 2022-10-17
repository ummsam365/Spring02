package service;

import java.util.List;

import vo.BoardVO;
import vo.JoVO;

public interface JoService {

	// ** selectList
	List<JoVO> selectList();
	// ** selectOne
	JoVO selectOne(JoVO vo);
	
	// ** Insert
	int insert(JoVO vo);
	// ** Update
	int update(JoVO vo);
	// ** Delete
	int delete(JoVO vo);
	
} //class