package com.tranv.workcv.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tranv.workcv.entity.ApplyPost;
import com.tranv.workcv.entity.Cv;
import com.tranv.workcv.entity.Recruitment;
import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.ApplyPostService;
import com.tranv.workcv.service.CvService;
import com.tranv.workcv.service.RecruitmentService;
import com.tranv.workcv.service.UserService;

@Controller
@RequestMapping("/apply")
public class ApplyJobController {
	@Autowired
	private UserService userService;

	@Autowired
	private CvService cvService;

	@Autowired
	private RecruitmentService recruitmentService;

	@Autowired
	private ApplyPostService applyPostService;

	// Retrieve the currently authenticated user.
	private User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User user = userService.findByEmail(email);
		return user;
	};

	/**
	 * Handle the request to apply for a job. This method sets the necessary
	 * attributes of the ApplyPost object, saves the ApplyPost to the database, and
	 * redirects the user to the home page.
	 */
	@PostMapping("apply-job1")
	public @ResponseBody String handleApplyJob1(@RequestParam("idRe") int idRe, @RequestParam("text") String text) {
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		User user = getUser();
		if (user == null) {
			return "false";
		} else {
			int theId = user.getId();
			Cv cv = cvService.getCvByUserId(theId);

			Recruitment recruitment = recruitmentService.getRecruitmentById(idRe);

			List<ApplyPost> applyPostsList = applyPostService.listApplyPostsByUser(user.getId());
			boolean isApplyPost = false;
			for (ApplyPost applyPost : applyPostsList) {
				if (applyPost.getRecruitment().getId() == idRe) {
					isApplyPost = true;
				}
			}
			if (isApplyPost) {
				return "";
			}

			ApplyPost applyPost = new ApplyPost();
			applyPost.setText(text);
			applyPost.setRecruitment(recruitment);
			applyPost.setCreatedAt(formater.format(new Date()));
			applyPost.setNameCv(cv.getFileName());
			applyPost.setUser(user);
			applyPost.setStatus(0);
			applyPostService.saveOrUpdateApplyPost(applyPost);
			return "true";

		}

	}

	/**
	 * Handle the request to apply for a job. This method sets the necessary
	 * attributes of the ApplyPost object, saves the ApplyPost to the database, and
	 * redirects the user to the home page.
	 */
	@SuppressWarnings("null")
	@PostMapping("/apply-job")
	@ResponseBody
	public String handleApplyJob(@RequestParam CommonsMultipartFile file, @RequestParam("idRe") int idRe,
			@RequestParam("text") String text, HttpSession session) {
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		User user = getUser();
		Recruitment recruitment = recruitmentService.getRecruitmentById(idRe);
		if (user == null) {
			return "false";
		} else {
			List<ApplyPost> applyPostsList = applyPostService.listApplyPostsByUser(user.getId());
			boolean isApplyPost = false;
			for (ApplyPost applyPost : applyPostsList) {
				if (applyPost.getRecruitment().getId() == idRe) {
					isApplyPost = true;
				}
			}
			if (isApplyPost) {
				return "";
			}

			int theId = user.getId();
			Cv cv = cvService.getCvByUserId(theId);
			String rootDir = session.getServletContext().getRealPath("/resources/upload/cvpdf");
			int index = file.getOriginalFilename().lastIndexOf(".");
			String extension = null;
			if (index > 0) {
				extension = file.getOriginalFilename().substring(index + 1);
			}
			if (!extension.equals("pdf")) {
				return "Error";

			}
			String fileName = "User_cv_" + user.getId() + "." + extension;
			Path filePath = Paths.get(rootDir, fileName);
			try {
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				e.printStackTrace();
				return "Error";
			}

			if (cv == null) {
				cv = new Cv();
			}
			cv.setUser(user);
			cv.setFileName(fileName);
			cvService.saveCv(cv);

			ApplyPost applyPost = new ApplyPost();
			applyPost.setText(text);
			applyPost.setRecruitment(recruitment);
			applyPost.setCreatedAt(formater.format(new Date()));
			applyPost.setNameCv(cv.getFileName());
			applyPost.setUser(user);
			applyPost.setStatus(0);
			applyPostService.saveOrUpdateApplyPost(applyPost);

			return "true";
		}

	}

}
