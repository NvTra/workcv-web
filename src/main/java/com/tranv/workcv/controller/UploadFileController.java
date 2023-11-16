package com.tranv.workcv.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.tranv.workcv.entity.Cv;
import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.CvService;
import com.tranv.workcv.service.UserService;

@Controller

public class UploadFileController {
	@Autowired
	private UserService userService;

	@Autowired
	private CvService cvService;

	private static final int THRESHOLD_SIZE = 1024 * 1024 * 3; // 3MB

	// save File
	@PostMapping("saveCvFile")
	public ModelAndView saveimage(@ModelAttribute("cv") Cv theCv, @RequestParam("userId") int theId,
			@RequestParam CommonsMultipartFile file, HttpSession session) throws Exception {

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(THRESHOLD_SIZE);
		File f = new File(System.getProperty("user.dir"));
		factory.setRepository(f);

		String fileName = file.getOriginalFilename();
		System.out.println(fileName);

		User theUser = userService.getUserById(theId);

		theCv.setUser(theUser);
		theCv.setFileName(fileName);
		cvService.saveCv(theCv);

		String helper = session.getServletContext().getRealPath("/");
		List y = Arrays.asList(helper.split("\\\\"));
		String rootDir = y.get(0) + java.io.File.separator + y.get(1) + java.io.File.separator + y.get(y.size() - 1)
				+ java.io.File.separator + "src\\main\\webapp\\image";
		String path = rootDir + File.separator + file.getOriginalFilename();
		byte[] bytes = file.getBytes();
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(path)));
		stream.write(bytes);
		stream.flush();
		stream.close();

		return new ModelAndView("redirect:/detail", "filesuccess", "File successfully saved!");
	}

	// Download a large file.
	@SuppressWarnings("unused")
	@GetMapping("/downloadFile")
	@ResponseBody
	public ResponseEntity<InputStreamResource> downloadLargeFile(@RequestParam("name") String fileName)
			throws Exception {
		File f = new File(System.getProperty("user.dir"));
		System.out.println(f);
		File file = new File(System.getProperty("user.dir") + File.separator + fileName);
		System.out.println(file);

		if (file == null) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
		}
		final InputStream inputStream = new FileInputStream(file);
		final InputStreamResource resource = new InputStreamResource(inputStream);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.LAST_MODIFIED, String.valueOf(file.lastModified()));
		httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
		httpHeaders.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
		return ResponseEntity.ok().headers(httpHeaders).contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}
}