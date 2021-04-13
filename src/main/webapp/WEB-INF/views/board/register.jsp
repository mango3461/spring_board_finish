<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<style>
	.uploadResult{
		width: 100%;
		background-color: skyblue;
	}
	.uploadResult ul {
		display: flex;
		flex-flow:row;
		justify-content:center;
		align-items:center;
	}
	.uploadResult ul li {
		list-style: none;
		padding: 10px;
	}
	.uploadResult ul li img{
		width: 20px;
	}
	
</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BmbxuPwQa2lc/FVzBcNJ7UAyJxM6wuqIj61tLrc4wSX0szH/Ev+nYRRuWlolflfl" crossorigin="anonymous">
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>글 작성하기</h1>
	<!-- 타겟 주소(action), 전송방식(method)를 작성해주세요
	그리고, 내부 폼에서는
	input태그의 text로 title 파라미터에 글제목을
	textarea태그로 content 파라미터에 글내용을
	input태그의 text로 writer 파라미터에 글쓴이를 적은 뒤
	input태그의 submit으로 제출하도록 작성해보세요. -->
	
	<form action="/board/register" method="post">
		제목 : <input class="form-control" type="text" name="title" /><br>
		본문: <br><textarea class="form-control" name="content"></textarea><br>
		글쓴이:<input class="form-control" type="text" name="writer" 
					value="${login.uname }" readonly /><br>
		<div class="uploadDiv">
			파일첨부:<input type="file" name="uploadFile" class="uploadInput" multiple>
		</div>
		<input id="submitBtn" class="btn btn-primary" type="submit" value="제출">
	</form>
	
	<h1>첨부 파일</h1>
	
	<div class="uploadResult">
		<ul>
			<!-- 업로드된 파일이 들어갈 자리 -->
		</ul>
	</div>
	<button id="uploadBtn">Upload</button>
	
	
	<script>
		$(document).ready(function(){
			
			var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
			var maxSize = 5242880; // 5MB
			
			function checkExtension(fileName, fileSize){
				if(fileSize >= maxSize){
					alert("파일사이즈 초과");
					return false;
				}
				
				if(regex.test(fileName)){
					alert("해당 종류의 파일은 업로드할 수 없습니다.");
					return false;
				}
				return true;
			}
			
			var cloneObj = $(".uploadDiv").clone();
			
			$('#uploadBtn').on("click", function(e){
				
				var formData = new FormData();
				
				var inputFile = $("input[name='uploadFile']");
				
				console.log(inputFile);
				
				var files = inputFile[0].files;
				
				console.log(files);
				
				// 파일 데이터를 폼에 집어넣기
				for(var i = 0; i < files.length; i++){
					if(!checkExtension(files[i].name, files[i].size)){
						return false;
					}
					
					formData.append("uploadFile", files[i]);
				}
				console.log(formData);
				
				$.ajax({
					url: '/uploadAjaxAction',
					processData: false,
					contentType: false,
					data: formData,
					type: 'POST',
					dataType: 'json',
					success: function(result){
						console.log(result);
						
						showUploadedFile(result);
						
						$(".uploadDiv").html(cloneObj.html());
					}
				});//ajax
			});//onclick uploadBtn			
			
			
			
			var uploadResult = $(".uploadResult ul");
			
			function showUploadedFile(uploadResultArr){
				var str = "";
				
				$(uploadResultArr).each(function(i, obj){
					
					if(!obj.fileType){
					var fileCallPath = encodeURIComponent(
										obj.uploadPath + "/"
									  + obj.uuid + "_" + obj.fileName);
					
						str += "<li "
							+ "data-path='" + obj.uploadPath + "' data-uuid='" + obj.uuid
							+ "' data-filename='" + obj.fileName + "' data-type='" + obj.fileType
							+ "'><a href='/download?fileName=" + fileCallPath
						    + "'>" + "<img src='/resources/attachicon.png'>"
							+	obj.fileName + "</a>"
							+ "<span data-file=\'" + fileCallPath + "\' data-type='file'> X </span>"
							+ "</li>";
					}else{
						//str += "<li>" + obj.fileName +"</li>";
						// 수정 후 코드
						
						var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" +
												obj.uuid + "_" + obj.fileName);
						console.log("파일경로:" + fileCallPath);
						str += "<li "
							+ "data-path='" + obj.uploadPath + "' data-uuid='" + obj.uuid
							+ "' data-filename='" + obj.fileName + "' data-type='" + obj.fileType
							+ "'><a href='/download?fileName=" + fileCallPath
							+ "'>" + "<img src='/display?fileName=" + fileCallPath 
							+ "'>" + obj.fileName + "</a>"
							+ "<span data-file=\'" + fileCallPath + "\' data-type='image'> X </span>"
							+ "</li>";
					}
					
					
				});
				
				uploadResult.append(str);
			}//showUpliadedFile
			
			$(".uploadResult").on("click", "span", function(e){
				var targetFile = $(this).data("file");
				var type = $(this).data("type");
				
				var targetLi = $(this).closest("li");
				
				$.ajax({
					url: '/deleteFile',
					data: {fileName: targetFile, type:type},
					dataType: 'text',
					type:'POST',
					success: function(result){
						alert(result);
						targetLi.remove();
					}
					
				});//ajax
				
			}); //click span	
			
			$("#submitBtn").on("click", function(e){
				// 1. 버튼 기능을 막으세요
				e.preventDefault();
				
				// 2. var formObj = $("form");로 폼태그를 가져옵니다.
				var formObj = $("form");
				
				// 3. formObj 내부에 64페이지 장표를 참고해서
				// hidden태그들을 순서대로 만들어줍니다.
				var str = "";
				
				$(".uploadResult ul li").each(function(i, obj){
					
					var jobj = $(obj);
					
					str += "<input type='hidden' name='attachList[" + i + "].fileName'"
						+ " value='" + jobj.data("filename") + "'>"
						+ "<input type='hidden' name='attachList[" + i + "].uuid'"
						+ " value='" + jobj.data("uuid") + "'>"
						+ "<input type='hidden' name='attachList[" + i + "].uploadPath'"
						+ " value='" + jobj.data("path") + "'>"
						+ "<input type='hidden' name='attachList[" + i + "].fileType'"
						+ " value='" + jobj.data("type") + "'>"
				});
				
				// 4. formObj.append()로 추가해줍니다.
				formObj.append(str);
				// 제출이 안 된다면 formObj의 상태를 확인합니다.
				console.log(formObj);
				
				// 5. formObj.submit()으로 제출합니다.
				formObj.submit();
			});
			
			
		
		});//document
	</script>


</body>
</html>