package org.ict.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.ict.domain.BoardAttachVO;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {
	
	private boolean checkImageType(File file) {
		try {
			String contentType = Files.probeContentType(file.toPath());
			
			return contentType.startsWith("image");
		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private String getFolder() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		
		String str = sdf.format(date);
		
		return str.replace("-", File.separator);
		
	}
	
	@PostMapping("/uploadAjaxAction")
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> uploadAjaxPost(MultipartFile[] uploadFile) {
		
		log.info("ajax post update!");
		
		List<BoardAttachVO> list = new ArrayList<>();
		String uploadFolder = "C:\\upload_data\\temp";
		
		
		String uploadFolderPath = getFolder();
		//폴더 생성
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("upload path: " + uploadPath);
		
		if(uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("-------------------");
			log.info("Upload file name: " + multipartFile.getOriginalFilename());
			log.info("Upload file size: " + multipartFile.getSize());

			//그림 정보를 브라우저로 전송하기 위해 그림정보를 java클래스로 먼저 저장
			//해야하는데 저장받을 객체부터 생성
			BoardAttachVO boardAttachVO = new BoardAttachVO();
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			uploadFileName = uploadFileName.substring(
					uploadFileName.lastIndexOf("\\") + 1);
			
			log.info("only file name : " + uploadFileName);
			
			// 현재 AttachFileDTO는 파일 이름과 uuid를 따로 저장하기때문에
			// 이를 반영해서 파일 이름만 먼저 저장합니다.
			boardAttachVO.setFileName(uploadFileName);
			
			
			UUID uuid = UUID.randomUUID();
			
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			//File saveFile = new File(uploadFolder, uploadFileName);
			
			try {
				File saveFile = new File(uploadPath, uploadFileName);
				multipartFile.transferTo(saveFile);
				
				// 전달할 이미지 정보에 uuid, uploadPath 추가하기
				boardAttachVO.setUuid(uuid.toString());
				boardAttachVO.setUploadPath(uploadFolderPath);
				
				// 썸네일 생성로직
				if(checkImageType(saveFile)) {
					//이미지인 경우 이미지 컬럼에 true 추가
					boardAttachVO.setFileType(true);
					
					FileOutputStream thumbnail = 
						new FileOutputStream(
								new File(uploadPath, "s_" + uploadFileName));
					Thumbnailator.createThumbnail(
							multipartFile.getInputStream(), thumbnail, 100, 100);
					thumbnail.close();
				}
				log.info(boardAttachVO);
				// 지금까지 저장한 이미지 정보 List<AttachFileDTO>에 넣기
				list.add(boardAttachVO);
				
			} catch(Exception e) {
				log.error(e.getMessage());
			}
		}//end for
		return new ResponseEntity<List<BoardAttachVO>>(list, 
												HttpStatus.OK);
	}
	
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		log.info("fileName: " + fileName);
		
		File file = new File("c:\\upload_data\\temp\\" + fileName);
		
		log.info("file: " + file);
		// 그림, 동영상 등의 파일은 바이트코드로 전송해야 손실이 없기 때문에
		// byte[] 자료형으로 바꿔서 전달하도록 처리합니다.
		ResponseEntity<byte[]> result = null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			
			header.add("Content-Type", 
						Files.probeContentType(file.toPath()));
			
			result = new ResponseEntity<>(
							FileCopyUtils.copyToByteArray(file),
							header,
							HttpStatus.OK);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@GetMapping(value="/download",
				produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(String fileName){
		
		log.info("download File : " + fileName);
		
		Resource resource = new FileSystemResource(
							"c:\\upload_data\\temp\\" + fileName);
		
		log.info("resoruce: " + resource);
		
		String resourceName = resource.getFilename();
		
		HttpHeaders headers = new HttpHeaders();
		
		try {
			headers.add("Content-Disposition", 
						"attachment; filename=" +
						new String(resourceName.getBytes("UTF-8"), 
										"ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource,
											headers,
											HttpStatus.OK);
	}
	
	
	@PostMapping("/deleteFile")
	@ResponseBody
	public ResponseEntity<String> deleteFile(String fileName, String type){
	
		log.info("deleteFile: " + fileName);
		
		File file = null;
		
		try {
			file = new File("c:\\upload_data\\temp\\" + 
							URLDecoder.decode(fileName, "UTF-8"));
			
			file.delete();
			
			if(type.equals("image")) {
				
				String largeFileName = file.getAbsolutePath().
												replace("s_", "");
				
				log.info("largeFileName : " + largeFileName);
				
				file = new File(largeFileName);
				
				file.delete();
			}
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}
	
	
}
