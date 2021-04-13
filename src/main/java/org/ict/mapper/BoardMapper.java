package org.ict.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.ict.domain.BoardVO;
import org.ict.domain.Criteria;
import org.ict.domain.SearchCriteria;

// 마이바티티스는 인터페이스 메서드를 정의한 다음
// xml파일에 메서드 실행시 동작시킬 쿼리문을 분리해 저장합니다.
// 따라 BoardMapper인터페이스에는 실행문 없이 메서드 선언만 합니다.
public interface BoardMapper {
	
	// ArrayList와 유사한 List(java.util.List)을 활용해
	// BoardVO를 여럿 담을수 있게 설정
	// 첫 번째 메서드는 테스트를 위해 어노테이션으로 쿼리문 작성 
	//@Select("SELECT * FROM ictboard where bno > 0")
	// 메서드가 작동함을 확인했기 때문에 쿼리문은 xml로 이전
	public List<BoardVO> getList();
	
	// insert구문은 자바 내부적으로 먼저 게시판에 필요한 자료들을
	// BoardVO 객체에 저장한 후, 쿼리문에 전달합니다.
	// 따라서 파라미터로 생성할 글에 대한 정보가 담긴
	// BoardVO 객체를 요구합니다.
	public void insert(BoardVO board);
	
	// read구문은 게시물 제목을 클릭했을때
	// 글 하나의 상세정보를 보여줍니다.
	// 따라서 리턴자료형은 글 하나에 해당하는 BoardVO로만 처리해도 되며
	// 글의 식별은 글 번호로 하기때문에 파라미터는 int bno입니다.
	// 이러면 글 번호 bno가 bno라는 이름으로 마이바티스에 전달됩니다.
	public BoardVO read(Long bno);
	
	// delete 구문은 게시물을 삭제할 때 번호로 구분해서 삭제합니다.
	// 따라서 역시 bno를 전달합니다.
	public int delete(Long bno);
	
	// delete, insert, update 구문은
	// return자료형이 원래는 void로 해도 상관없으나
	// row 몇 개가 변경되었는지는 체크할 필요가 있기때문에
	// return자료형에 int자료형을 정의하는것을 허용합니다.
	// 이 경우, mapper xml에서는 resultType을 명시하지 않습니다.
	public int update(BoardVO board);

	// 페이지 정보를 함께 받아 목록을 출력하는 메서드 작성
	public List<BoardVO> listPage(SearchCriteria cri);

	// 전체 글 갯수를 로딩할때마다 받아오기 위해 처리
	public int countPageNum(SearchCriteria cri);
	
	public void updateReplyCount(@Param("bno") Long bno,
								@Param("amount") int amount);

	public long getMaxBno();
	
}






