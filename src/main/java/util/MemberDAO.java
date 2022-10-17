package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import vo_OLD.MemberVO;

//** DAO (Data Access Object)
// => CRUD 구현 
// C: create -> insert
// R: read   -> selectList, selectOne
// U: update -> update
// D: delete -> delete

//@Repository
public class MemberDAO {
	// ** 전역변수 정의 
	Connection cn = DBConnection.getConnection();
	Statement st;
	PreparedStatement pst;
	ResultSet rs;
	String sql;
	
	// ** selectList
	public List<MemberVO> selectList() {
		sql = "select * from member";
		List<MemberVO> list = new ArrayList<MemberVO>();
		try {
			st=cn.createStatement();
			rs=st.executeQuery(sql);
			// => 출력자료가 있는지 확인
			//    존재하면 요청한 객체로 전달
			if (rs.next()) {
				// => 출력자료를 1 row 씩 -> vo에 set -> list에 add
				do {
					MemberVO vo = new MemberVO();
					vo.setId(rs.getString(1));
					vo.setPassword(rs.getString(2));
					vo.setName(rs.getString("name"));
					vo.setInfo(rs.getString(4));
					vo.setBirthday(rs.getString(5));
					vo.setJno(rs.getInt(6));
					vo.setAge(rs.getInt(7));
					vo.setPoint(rs.getDouble(8));
					list.add(vo);
				}while(rs.next());
				return list;
			}else {
				System.out.println("** selectList : 출력자료가 1건도 없음 **");
				return null;
			}
		} catch (Exception e) {
			System.out.println("** selectList Exception => "+e.toString());
			return null;
		}
	} //selectList
	
	// ** selectOne
	public MemberVO selectOne(MemberVO vo) {
		sql = "select * from member where id=?";
		try {
			pst = cn.prepareStatement(sql);
			pst.setString(1, vo.getId());
			rs=pst.executeQuery();
			// => 결과확인
			if (rs.next()) {
				vo.setId(rs.getString(1));
				vo.setPassword(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setInfo(rs.getString(4));
				vo.setBirthday(rs.getString(5));
				vo.setJno(rs.getInt(6));
				vo.setAge(rs.getInt(7));
				vo.setPoint(rs.getDouble(8));
				return vo;
			}else {
				System.out.println("** id 에 해당하는 Member는 없습니다. **");
				return null;
			}
		} catch (Exception e) {
			System.out.println("** selectOne Exception => "+e.toString());
			return null;
		}
	} //selectOne
	
	// ** Insert
	// Join -> Insert
	public int insert(MemberVO vo) {
		sql="insert into member values(?,?,?,?,?,?,?,?)";
		try {
			pst=cn.prepareStatement(sql);
			pst.setString(1, vo.getId());
			pst.setString(2, vo.getPassword());
			pst.setString(3, vo.getName());
			pst.setString(4, vo.getInfo());
			pst.setString(5, vo.getBirthday());
			pst.setInt(6, vo.getJno());
			pst.setInt(7, vo.getAge());
			pst.setDouble(8, vo.getPoint());
			return pst.executeUpdate();
			// executeUpdate() => 처리된 row 의 갯수 return
		} catch (Exception e) {
			System.out.println("** Member_insert Exception => "+e.toString());
			return 0;
		}
	} //insert
	
	// ** Update
	// => P.Key 인 ID 를 제외한 모든 컬럼 수정
	public int update(MemberVO vo) {
		sql="update member set password=?, name=?, info=?, birthday=?,"
				+ " jno=?, age=?, point=? where id=?";
		try {
			pst=cn.prepareStatement(sql);
			pst.setString(1, vo.getPassword());
			pst.setString(2, vo.getName());
			pst.setString(3, vo.getInfo());
			pst.setString(4, vo.getBirthday());
			pst.setInt(5, vo.getJno());
			pst.setInt(6, vo.getAge());
			pst.setDouble(7, vo.getPoint());
			pst.setString(8, vo.getId());
			return pst.executeUpdate();
			// executeUpdate() => 처리된 row 의 갯수 return
		} catch (Exception e) {
			System.out.println("** Member_update Exception => "+e.toString());
			return 0;
		}
	} //update
	
	// ** Delete
	public int delete(MemberVO vo) {
		sql="delete from member where id=?";
		try {
			pst=cn.prepareStatement(sql);
			pst.setString(1, vo.getId());
			return pst.executeUpdate();
			// executeUpdate() => 처리된 row 의 갯수 return
		} catch (Exception e) {
			System.out.println("** Member_delete Exception => "+e.toString());
			return 0;
		}
	} //delete

} //class
