<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BmbxuPwQa2lc/FVzBcNJ7UAyJxM6wuqIj61tLrc4wSX0szH/Ev+nYRRuWlolflfl" crossorigin="anonymous">
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
  <div class="container">
  	<div class="row">
  		<h1 class="text-primary text-center">전체 글 목록</h1>
  	</div>
  	<div class="row">
  		<div class="text-body">
  			<select name="searchType">
  				<option value="n"
  				<c:out value="${cri.searchType == null ? 'selected' : '' }" />>	
  				-
  				</option>
  				<option value="t"
  				<c:out value="${cri.searchType eq 't' ? 'selected' : '' }" />>	
  				제목
  				</option>
  				<option value="c"
  				<c:out value="${cri.searchType eq 'c' ? 'selected' : '' }" />>	
  				본문
  				</option>
  				<option value="w"
  				<c:out value="${cri.searchType eq 'w' ? 'selected' : '' }" />>	
  				글쓴이
  				</option>
  				<option value="tc"
  				<c:out value="${cri.searchType eq 'tc' ? 'selected' : '' }" />>	
  				제목+본문
  				</option>
  				<option value="cw"
  				<c:out value="${cri.searchType eq 'cw' ? 'selected' : '' }" />>	
  				본문+글쓴이
  				</option>
  				<option value="tcw"
  				<c:out value="${cri.searchType eq 'tcw' ? 'selected' : '' }" />>	
  				제목+본문+글쓴이
  				</option>
  			</select>
  			
  			<input type="text"
  					name="keyword"
  					id="keywordInput"
  					value="${cri.keyword }">
  			<button id="searchBtn">Search</button>
  			
  		</div>
  	</div>
  	
    <div class="row">
      <table class="table table-hover">
        <thead>
	        <tr>
	          <th>글번호</th>
	          <th>글제목</th>
	          <th>글쓴이</th>
	          <th>작성일</th>
	          <th>수정일</th>
	        </tr>
        </thead>
        <tbody>
          <!-- forEach구문은 반복적으로 html요소를 표현할 때
          사용하는 태그라이브러리 입니다.
          원래 스크립트릿을 써도 같은 기능을 구현할 수 있지만
          자바코드와 html코드가 섞이는 상황을 막기 위해 씁니다. -->
          <c:forEach var="board" items="${list }">
	          <tr>
		          <td>${board.bno }</td>
		          <td><a href="/board/get?bno=${board.bno}&page=${cri.page}&searchType=${cri.searchType}&keyword=${cri.keyword}">
		          		${board.title } [ ${board.replyCount } ]</a></td>
		          <td>${board.writer }</td>
		          <td>${board.regDate }</td>
		          <td>${board.updateDate }</td>
		      </tr>
          </c:forEach>
        </tbody>
      </table>

    </div>
    <div class="row">
	  <ul class="col-md-11 pagination justify-content-center">
	    <!-- 이전 페이지 버튼 -->
	    <c:if test="${pageMaker.prev }">
	    	<li class="page-item">
	    		<a class="page-link"
	    			href="/board/list?page=${pageMaker.startPage -1 }&searchType=${cri.searchType}&keyword=${cri.keyword}">
	    			&laquo;	
	    		</a>
	    	</li>
	    </c:if>
	    
	    <!-- 페이지 번호 버튼 -->
	    <c:forEach begin="${pageMaker.startPage }"
	    			end="${pageMaker.endPage }"
	    			var="idx">		
	    	<li class="page-item
	    		<c:out value="${pageMaker.cri.page == idx ? 'active' : '' }" />">
	    		<a class="page-link"
	    			href="/board/list?page=${idx }&searchType=${cri.searchType}&keyword=${cri.keyword}">${idx }</a>
	    	</li>
	    </c:forEach>
	    
	    <!-- 다음 페이지 버튼 -->
	    <c:if test="${pageMaker.next && pageMaker.endPage >0 }">
	    	<li class="page-item">
	    		<a class="page-link"
	    			href="/board/list?page=${pageMaker.endPage +1 }&searchType=${cri.searchType}&keyword=${cri.keyword}">
	    			&raquo;	
	    		</a>
	    	</li>
	    </c:if>
	    
	  </ul>
	  <a class="col-md-1 btn btn-primary btn-sm" 
	  	href="/board/register">글쓰기</a>
    </div>


  </div><!-- div container end -->
  <!-- 하단에 Script태그를 이용해 ${bno}를 콘솔에 출력하는
  구문을 작성해 list페이지의 개발자도구 console창에 출력해보세요. -->
	<script type="text/javascript">
		$(document).ready(function(){
			
			// 삭제된 글 번호는 controller에서 넘어옵니다.
			// ${bno}라는 명칭으로 넘어오므로 변수에 저장합니다.
			// 문자열 형태로 받아오도록 처리
			// 그렇지 않으면 콘솔창에서 받은 자료가 없을때 에러가 남
			var bno = "${bno}";
			
			// 받아 온 bno를 출력
			console.log(bno);
			
			// alert()구문을 이용해 글을 삭제할때마다
			// n번 글이 삭제되었습니다 라는 안내문구를 띄워주세요.
			// 조건문을 이용해 삭제일때만 실행하도록 로직을 수정합니다.
			if(bno !== ''){
				alert(bno + "번 글이 삭제되었습니다.");
			}
			
			//검색버튼 작동
			$('#searchBtn').on("click", function(event){
				self.location = "list"
					+ "?page=1"
					+ "&searchType="
					+ $("select option:selected").val()
					+ "&keyword=" + $("#keywordInput").val();
			});
		});
	</script>
</body>
</html>








