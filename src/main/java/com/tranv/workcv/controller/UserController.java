package com.tranv.workcv.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tranv.workcv.entity.ApplyPost;
import com.tranv.workcv.entity.Company;

import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.ApplyPostService;
import com.tranv.workcv.service.CompanyService;

import com.tranv.workcv.service.UserService;
import com.tranv.workcv.until.Pagination;
import com.tranv.workcv.until.UploadFileUtil;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ApplyPostService applyPostService;

	// Retrieve the currently authenticated user.
	private User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User theUser = userService.findByEmail(email);
		return theUser;
	};

	// Update the user's profile.
	@PostMapping("/update-profile")
	public String updateProfile(@ModelAttribute("user") User theUser) {

		userService.update(theUser);
		return "redirect:/detail";
	}

	@PostMapping("/upload")
	public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("email") String email, HttpSession session) {
		System.out.println("email" + email);
		try {
			User user = getUser();
			String rootDir = UploadFileUtil.UPLOAD_DIR("image", session);

			int index = file.getOriginalFilename().lastIndexOf('.');

			String extension = null;
			if (index > 0) {
				extension = file.getOriginalFilename().substring(index + 1);
			}
			String fileName = "User_img_" + user.getId() + "." + extension;
			System.out.println(fileName);

			Path filePath = Paths.get(rootDir, fileName);

			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			user.setImage(fileName);
			userService.update(user);

			String urlImg = "resources/upload/image/" + user.getImage();

			return urlImg;
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
	}

	@PostMapping("/uploadCv")
	public @ResponseBody String handleFileUploadCv(@RequestParam("file") MultipartFile file, HttpSession session) {

		try {
			User user = getUser();
			String rootDir = UploadFileUtil.UPLOAD_DIR("cvpdf", session);

			int index = file.getOriginalFilename().lastIndexOf('.');

			String extension = null;
			if (index > 0) {
				extension = file.getOriginalFilename().substring(index + 1);
			}
			String fileName = "User_cv_" + user.getId() + "." + extension;
			System.out.println(fileName);

			Path filePath = Paths.get(rootDir, fileName);

			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			user.setImage(fileName);
			userService.update(user);

			String urlImg = "resources/upload/cvpdf/" + fileName;

			return urlImg;
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
	}

	// Update the user's company information.
	@PostMapping("/update-company")
	public String updateCompany(@ModelAttribute("company") Company theCompany) {
		User theUser = getUser();
		theCompany.setStatus(1);
		theCompany.setUser(theUser);
		companyService.saveOrUpdateCompany(theCompany);
		return "redirect:/detail";
	}

	// Update the user's company logo.
	@PostMapping("/update-logo-company")
	public String updateLogoCompany(@RequestParam("id") int theId,
			@RequestParam("logoCompany") MultipartFile multipartFile) throws IOException {
		Company company = companyService.getCompanyById(theId);
		byte[] logo = multipartFile.getBytes();
		company.setLogo(logo);
		companyService.saveOrUpdateCompany(company);
		return "redirect:/detail";
	}

	// Confirm the user's account.
	@PostMapping("/confirm-account")
	public String confirmAccount() {
		User theUser = getUser();
		theUser.setStatus(1);
		userService.update(theUser);
		return "redirect:/detail";
	}

	// Display the form for posting a job under a company.
	@GetMapping("/post-company")
	public String postCompany(@RequestParam("companyId") int companyId, Model theModel) {
		Company theCompany = companyService.getCompanyById(companyId);
		theModel.addAttribute("company", theCompany);
		return "post-company";
	}

	// Display a paginated list of user applications.
	@GetMapping("/list-user")
	public String listUser(@RequestParam(name = "page", defaultValue = "1") int currentPage, Model theModel) {
		User theUser = getUser();
		int userId = theUser.getId();
		Company theCompany = companyService.getCompanyByUserId(userId);
		int companyId = theCompany.getId();
		List<ApplyPost> applyPosts = applyPostService.listApplyPostsByCompany(companyId);
		Pagination.pagination(applyPosts, currentPage, theModel);
		return "public/list-user";
	}

}
