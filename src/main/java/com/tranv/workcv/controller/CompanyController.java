package com.tranv.workcv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tranv.workcv.entity.Company;
import com.tranv.workcv.entity.Recruitment;
import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.CompanyService;
import com.tranv.workcv.service.FollowCompanyService;
import com.tranv.workcv.service.RecruitmentService;
import com.tranv.workcv.service.UserService;

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
	@GetMapping("/post-company")
	public String postCompany(@RequestParam(name = "page", defaultValue = "1") int currentPage,
			@RequestParam("companyId") int CompanyId, Model theModel) {
		Company theCompany = companyService.getCompanyById(CompanyId);
		List<Recruitment> recruitments = recruitmentService.getResultRecruitmentByCompany(CompanyId);
		int itemsPerPage = 5;
		int totalPages = (int) Math.ceil((double) recruitments.size() / itemsPerPage);
		int startIndex = (currentPage - 1) * itemsPerPage;
		List<Recruitment> currentPageDonations = recruitments.subList(startIndex,
				Math.min(startIndex + itemsPerPage, recruitments.size()));
		theModel.addAttribute("company", theCompany);
		theModel.addAttribute("currentPage", currentPage);
		theModel.addAttribute("totalPages", totalPages);
		theModel.addAttribute("recruitments", currentPageDonations);
		return "post-company";
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
		return "detail-company";
	}

	// Handle the request to follow a company.
	@PostMapping("/follow-company")
	public String followCompany(@RequestParam("companyId") int companyId, Model theModel) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User theUser = userService.findByEmail(email);
		int userId = theUser.getId();
		followCompanyService.followCompany(userId, companyId);
		return "redirect:/company/detail-company?companyId=" + companyId;
	}

	// Handle the request to unfollow a company.
	@PostMapping("/unfollow-company")
	public String unFollowCompany(@RequestParam("companyId") int companyId, Model theModel) {
		User theUser = getUser();
		int userId = theUser.getId();
		followCompanyService.unFollowCompany(userId, companyId);
		return "redirect:/company/detail-company?companyId=" + companyId;
	}

	// Handle the request to unfollow a company and redirect to the list of followed
	// companies.
	@PostMapping("/unfollow-company2")
	public String unFollowCompany2(@RequestParam("companyId") int companyId, Model theModel) {
		User theUser = getUser();
		int userId = theUser.getId();
		followCompanyService.unFollowCompany(userId, companyId);
		return "redirect:/company/list-follow-company";
	}

	// Handle the request to view the list of followed companies.
	@GetMapping("/list-follow-company")
	public String listFollowCompany(@RequestParam(name = "page", defaultValue = "1") int currentPage, Model theModel) {
		User theUser = getUser();
		int theId = theUser.getId();
		List<Company> companies = followCompanyService.listCompanyFollow(theId);
		int itemsPerPage = 5;
		int totalPages = (int) Math.ceil((double) companies.size() / itemsPerPage);
		int startIndex = (currentPage - 1) * itemsPerPage;
		List<Company> currentPageDonations = companies.subList(startIndex,
				Math.min(startIndex + itemsPerPage, companies.size()));
		theModel.addAttribute("currentPage", currentPage);
		theModel.addAttribute("totalPages", totalPages);
		theModel.addAttribute("companies", currentPageDonations);
		return "list-follow-company";

	}

}
