package com.tranv.workcv.until;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public class UploadFileUtil {

	public static String UPLOAD_DIR(String fileTyle, HttpSession session) {
		String helper = session.getServletContext().getRealPath("/");
		List y = Arrays.asList(helper.split("\\\\"));
		String rootDir = y.get(0) + java.io.File.separator + y.get(1) + java.io.File.separator + y.get(y.size() - 1)
				+ java.io.File.separator + "src\\main\\webapp\\resources\\upload\\" + fileTyle;
		
		
		return rootDir;
	}

	public static void handleFileUpload(MultipartFile file, HttpSession session, String fileTyle, String fileName) {
		// setup folder
		String helper = session.getServletContext().getRealPath("/");
		List y = Arrays.asList(helper.split("\\\\"));
		String rootDir = y.get(0) + java.io.File.separator + y.get(1) + java.io.File.separator + y.get(y.size() - 1)
				+ java.io.File.separator + "upload\\" + fileTyle;

		// get extension
		try {
			int index = file.getOriginalFilename().lastIndexOf('.');
			String extension = null;
			if (index > 0) {
				extension = file.getOriginalFilename().substring(index + 1);
			}
			Path filePath = Paths.get(rootDir, fileName + "." + extension);
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
