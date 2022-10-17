package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import criTest.Criteria;
import criTest.SearchCriteria;
import mapperInterface.BoardMapper;
import mapperInterface.JoMapper;
import util.BoardDAO;
import vo.BoardVO;
import vo.JoVO;

//** interface 자동완성 
//=> Alt + Shift + T  
//=> 또는 마우스우클릭 PopUp Menu 의  Refactor - Extract Interface...

@Service
public class JoServiceImpl implements JoService {
	
	@Autowired
	JoMapper mapper;
	
	// ** selectList
	@Override
	public List<JoVO> selectList() {
		return mapper.selectList();
	}
	// ** selectOne
	@Override
	public JoVO selectOne(JoVO vo) {
		return mapper.selectOne(vo);
	}
	// ** Insert
	@Override
	public int insert(JoVO vo) {
		return mapper.insert(vo);
	}
	// ** Update
	@Override
	public int update(JoVO vo) {
		return mapper.update(vo);
	}
	// ** Delete
	@Override
	public int delete(JoVO vo) {
		return mapper.delete(vo);
	}

} //class
