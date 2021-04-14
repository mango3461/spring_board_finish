package org.ict.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.ict.domain.BoardAttachVO;
import org.ict.mapper.BoardAttachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class FileCheckTask {

	@Autowired
	private BoardAttachMapper attachMapper;
	
	private String getFolderYesterDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal = Calendar.getInstance();
		
		cal.add(Calendar.DATE, -1);
		
		String str = sdf.format(cal.getTime());
		
		return str.replace("-", File.separator);
	}
	
	@Scheduled(cron="0 0 2 * * *")
	public void checkFiles() throws Exception {
		
		log.warn("File Check Task run....");
		log.warn(new Date());
		// get file list in db
		List<BoardAttachVO> fileList = attachMapper.getOldFiles();
		
		//check file and directory path
		List<Path> fileListPaths = fileList.stream().map(
				//폴더 구분을 ,로 하고 파일이름은 +로 다 합쳐준다
				vo -> Paths.get("c:\\upload_data\\temp", vo.getUploadPath(),
								vo.getUuid() + "_" + vo.getFileName()))
				.collect(Collectors.toList());
		
		//image file thumbnail
		fileList.stream().filter(vo -> vo.isFileType() == true)
				.map(vo -> Paths.get("c:\\upload_data\\temp", vo.getUploadPath(), "s_" + 
									vo.getUuid() + "_" + vo.getFileName()))
				.forEach(p -> fileListPaths.add(p));
		log.warn("========================");
		
		fileListPaths.forEach(p -> log.warn(p));
		
		//files in yesterday directory
		File targetDir = Paths.get("c:\\upload_data\\temp", getFolderYesterDay()).toFile();
		
		File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false);
		
		log.warn("-------------------------");
		for(File file : removeFiles) {
			log.warn(file.getAbsoluteFile());
			file.delete();
		}
	}
}
