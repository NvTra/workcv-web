package com.tranv.workcv.controller;

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
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tranv.workcv.entity.Company;

import com.tranv.workcv.entity.Recruitment;
import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.CompanyService;
import com.tranv.workcv.service.FollowCompanyService;
import com.tranv.workcv.service.RecruitmentService;
import com.tranv.workcv.service.UserService;
import com.tranv.workcv.until.PaginationUtil;

@Controller
@RequestMapping("/company")
public class CompanyController {
	@Autowired
	private CompanyService companyService;

	@Autowired
	private RecruitmentService recruitmentService;

	@Autowired
	private FollowCompanyService followCompanyService;

	@Autowired
	private UserService userService;

	// Method to get user login information
	private User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User theUser = userService.findByEmail(email);
		return theUser;
	}

	// Handle the request to post a company.
	@GetMapping("/company-post")
	public String postCompany(@RequestParam(name = "page", defaultValue = "1") int currentPage,
			@RequestParam("companyId") int CompanyId, Model theModel) {
		Company theCompany = companyService.getCompanyById(CompanyId);
		List<Recruitment> recruitments = recruitmentService.getResultRecruitmentByCompany(CompanyId);
		PaginationUtil.pagination(recruitments, currentPage, theModel);
		theModel.addAttribute("company", theCompany);
		return "public/post-company";
	}

	@SuppressWarnings("null")
	@PostMapping("/upload-company")
	public @ResponseBody String handleFileUploadCv(@RequestParam("file") MultipartFile file, HttpSession session) {
		User user = getUser();
		Company company = companyService.getCompanyByUserId(user.getId());
		if (company == null) {
			company = new Company();
			company.setUser(user);
			companyService.saveOrUpdateCompany(company);
		}
		try {

			String rootDir = session.getServletContext().getRealPath("/resources/upload/company");

			int index = file.getOriginalFilename().lastIndexOf('.');

			String extension = null;
			if (index > 0) {
				extension = file.getOriginalFilename().substring(index + 1);
			}
			String fileName = "Company_logo_" + company.getId() + "." + extension;
			Path filePath = Paths.get(rootDir, fileName);

			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			// save cv in db
			company.setUser(user);
			company.setLogo(fileName);
			companyService.saveOrUpdateCompany(company);
			String urlImg = "resources/upload/company/" + fileName;

			return urlImg;
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
	}

	// Handle the request to view the details of a company.
	@GetMapping("/detail-company")
	public String detailCompany(@RequestParam("companyId") int companyId, Model theModel) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User theUser = userService.findByEmail(email);
		Company theCompany = companyService.getCompanyById(companyId);

		if (theUser != null) {
			boolean isFollowed = theUser.getCompanies().stream()
					.anyMatch(followedCompany -> followedCompany.getId() == theCompany.getId());
			theModel.addAttribute("theUser", theUser);
			theModel.addAttribute("isFollowed", isFollowed);
		}

		theModel.addAttribute("company", theCompany);
		return "public/detail-company";
	}

	// Handle the request to follow a company.
	@PostMapping("/follow-company")
	public @ResponseBody String followCompany(@RequestParam("companyId") int companyId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User theUser = userService.findByEmail(email);
		Company theCompany = companyService.getCompanyById(companyId);
		if (theUser == null) {
			return "false";
		} else {
			boolean isFollowed = theUser.getCompanies().stream()
					.anyMatch(followedCompany -> followedCompany.getId() == theCompany.getId());
			int userId = theUser.getId();
			if (isFollowed) {
				followCompanyService.unFollowCompany(userId, companyId);
				return "";
			} else {
				followCompanyService.followCompany(userId, companyId);
				return "true";
			}

		}
	}

	// Handle the request to unfollow a company.
	@PostMapping("/unfollow-company")
	public String unFollowCompany(@RequestParam("companyId") int companyId) {
		User theUser = getUser();
		int userId = theUser.getId();
		followCompanyService.unFollowCompany(userId, companyId);
		return "redirect:/company/detail-company?companyId=" + companyId;
	}

	// Handle the request to unfollow a company and redirect to the list of followed
	// companies.
	@GetMapping("/delete-follow")
	public String unFollowCompany2(@RequestParam("companyId") int companyId) {
		User theUser = getUser();
		int userId = theUser.getId();
		followCompanyService.unFollowCompany(userId, companyId);
		return "redirect:/user/get-list-company";
	}

}
