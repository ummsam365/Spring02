package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import vo_OLD.BoardVO;
import vo_OLD.MemberVO;

//** DAO (Data Access Object)
// => CRUD 구현 
// C: create -> insert
// R: read   -> selectList, selectOne
// U: update -> update
// D: delete -> delete

@Repository
public class BoardDAO {
	// ** 전역변수 정의 
	Connection cn = DBConnection.getConnection();
	Statement st;
	PreparedStatement pst;
	ResultSet rs;
	String sql;
	
	// ** selectList
	public List<BoardVO> selectList() {
		sql = "select seq, id, title, regdate, cnt, root, step, indent from board order by root desc, step asc";
		List<BoardVO> list = new ArrayList<BoardVO>();
		try {
			st=cn.createStatement();
			rs=st.executeQuery(sql);
			// => 출력자료가 있는지 확인
			//    존재하면 요청한 객체로 전달
			if (rs.next()) {
				// => 출력자료를 1 row 씩 -> vo에 set -> list에 add
				do {
					BoardVO vo = new BoardVO();
					vo.setSeq(rs.getInt(1));
					vo.setId(rs.getString(2));
					vo.setTitle(rs.getString(3));
					vo.setRegdate(rs.getString(4));
					vo.setCnt(rs.getInt(5));
					vo.setRoot(rs.getInt(6));
					vo.setStep(rs.getInt(7));
					vo.setIndent(rs.getInt(8));
					list.add(vo);
				}while(rs.next());
				return list;
			}else {
				System.out.println("** B selectList : 출력자료가 1건도 없음 **");
				return null;
			}
		} catch (Exception e) {
			System.out.println("** B selectList Exception => "+e.toString());
			return null;
		}
	} //selectList
	
	// ** selectOne
	public BoardVO selectOne(BoardVO vo) {
		sql = "select * from board where seq=?";
		try {
			pst = cn.prepareStatement(sql);
			pst.setInt(1, vo.getSeq());
			rs=pst.executeQuery();
			// => 결과확인
			if (rs.next()) {
				vo.setSeq(rs.getInt(1));
				vo.setId(rs.getString(2));
				vo.setTitle(rs.getString(3));
				vo.setContent(rs.getString(4));
				vo.setRegdate(rs.getString(5));
				vo.setCnt(rs.getInt(6));
				vo.setRoot(rs.getInt(7));
				vo.setStep(rs.getInt(8));
				vo.setIndent(rs.getInt(9));
				return vo;
			}else {
				System.out.println("** seq 에 해당하는 글자료는 없습니다. **");
				return null;
			}
		} catch (Exception e) {
			System.out.println("** B selectOne Exception => "+e.toString());
			return null;
		}
	} //selectOne
	
	// ** Insert
	// => 새글등록 -> Insert
	// => 답글등록 추가후 원글 : root-> seq 와 동일 , step 과 indent 는 0
	
	// => MySql : last_insert_id 함수는 테이블의 마지막 auto_increment 값을 리턴
	// 		- LAST_INSERT_ID()는 동일 세션에서는 제대로 동작하지만 세션이 다를 경우 0 을 return 함.
	//   	  그러므로 JDBC 에서는 적용 불가능.
	//		"insert into board(id,title,content,root) values(?,?,?,(LAST_INSERT_ID()+1))";
	// => MySql : subQuery -> JDBC 적용
	//		-  select IFNULL(max(seq),0)+1 from board  : JDBC 적용안됨
	//		-  (select * from (select IFNULL(max(seq),0)+1 from board) as temp) : 적용됨 
	// => MySql : seq 는 auto_increment, root 는 subQuery_IFNULL 적용시    
	// 		- 마지막 글 삭제후 바로 입력했을때 두개의 값이 다를수 있기 때문에
	//		  seq, root 모두 subQuery_IFNULL 을 적용하는것이 바람직함.
	
	public int insert(BoardVO vo) {
//		sql="insert into board(id,title,content,root) "
//		   + "values(?,?,?,(select * from (select IFNULL(max(seq),0)+1 from board) as temp))";
		sql="insert into board(seq,id,title,content,root) values("
 				   + "(select * from (select IFNULL(max(seq),0)+1 from board) as temp),"
 				   + "?,?,?,(select * from (select IFNULL(max(seq),0)+1 from board) as temp))";
		try {
			pst=cn.prepareStatement(sql);
			pst.setString(1, vo.getId());
			pst.setString(2, vo.getTitle());
			pst.setString(3, vo.getContent());
			return pst.executeUpdate();
			// executeUpdate() => 처리된 row 의 갯수 return
		} catch (Exception e) {
			System.out.println("** Board_insert Exception => "+e.toString());
			return 0;
		}
	} //insert
	
	// ** Update
	// => 글수정: Title,  Content 컬럼만 수정가능
	public int update(BoardVO vo) {
		sql="update board set title=?, content=? where seq=?";
		try {
			pst=cn.prepareStatement(sql);
			pst.setString(1, vo.getTitle());
			pst.setString(2, vo.getContent());
			pst.setInt(3, vo.getSeq());
			return pst.executeUpdate();
			// executeUpdate() => 처리된 row 의 갯수 return
		} catch (Exception e) {
			System.out.println("** Board_update Exception => "+e.toString());
			return 0;
		}
	} //update
	
	// ** Delete
	// => 답글 추가후
	//	  - 원글삭제시 : 하위글모두 삭제
	//    - 답글삭제시 : 해당답글만 삭제
	// => 이를 위해서는 vo에 root 값이 담겨있어야함
	public int delete(BoardVO vo) {
		// 원글삭제 or 답글삭제 확인
		if ( vo.getSeq()==vo.getRoot() ) {
				// 원글 -> 동일 root 모두 삭제
				// 		  원글은 seq와 root가 동일하므로 
				//		  where 의 비교 컬럼만 root 이고, ? 는 seq로 가능
			sql="delete from board where root=?";
		}else { // 답글 -> 해당하는 seq 만 삭제
			sql="delete from board where seq=?";
		}
		try {
			pst=cn.prepareStatement(sql);
			pst.setInt(1, vo.getSeq());
			return pst.executeUpdate();
			// executeUpdate() => 처리된 row 의 갯수 return
		} catch (Exception e) {
			System.out.println("** Member_delete Exception => "+e.toString());
			return 0;
		}
	} //delete
	
	// ** 조회수 증가
	public int countUp(BoardVO vo) {
		sql="update board set cnt=cnt+1 where seq=?";
		try {
			pst=cn.prepareStatement(sql);
			pst.setInt(1, vo.getSeq());
			return pst.executeUpdate();
			// executeUpdate() => 처리된 row 의 갯수 return
		} catch (Exception e) {
			System.out.println("** Board_countUp Exception => "+e.toString());
			return 0;
		}
	} //countUp
	
	// ** Reply Insert
	// => 답글등록 -> rinsert
	// => 답글등록시에는 stepUpdate 가 필요함.
	//    답글 입력 성공후 stepUpdate 실행
	//	  -> 현재 입력된 답글의 step 값은 수정되지 않도록 sql 구문의 조건 주의 	
	// => JDBC sunQuery 구문 적용시 주의사항
	//	  -> select 구문으로 한번더 씌워 주어야함 (insert 의 경우에도 동일)
	public int stepUpdate(BoardVO vo) {
		sql="update board set step=step+1 where root=? and step>=?"
				+ " and seq <> (select * from (select max(seq) from board) as temp)";
		try {
			pst=cn.prepareStatement(sql);
			pst.setInt(1, vo.getRoot());
			pst.setInt(2, vo.getStep());
			return pst.executeUpdate();
			// executeUpdate() => 처리된 row 의 갯수 return
		} catch (Exception e) {
			System.out.println("** Board_stepUpdate Exception => "+e.toString());
			return 0;
		}
	} //stepUpdate
	
	public int rinsert(BoardVO vo) {
		sql="insert into board(seq,id,title,content,root,step,indent) values("
				 + "(select * from (select IFNULL(max(seq),0)+1 from board) as temp),"
				 + "?,?,?,?,?,?)";
		int result = 0;
		try {
			pst=cn.prepareStatement(sql);
			pst.setString(1, vo.getId());
			pst.setString(2, vo.getTitle());
			pst.setString(3, vo.getContent());
			pst.setInt(4, vo.getRoot());
			pst.setInt(5, vo.getStep());
			pst.setInt(6, vo.getIndent());
			result = pst.executeUpdate();
			// => executeUpdate() => 처리된 row 의 갯수 return
			// => 답글 입력 성공후 stepUpdate
			// 	  자신의 step 값은 변경되지않도록 stepUpdate sql 구문의 조건 주의 
			if ( result > 0 ) {
				System.out.println("** stepUpdate Count => "+ stepUpdate(vo));
			}
		} catch (Exception e) {
			System.out.println("** Board_insert Exception => "+e.toString());
		}
		return result;
	} //rinsert

} //class
