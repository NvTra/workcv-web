package com.tranv.workcv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tranv.workcv.entity.ApplyPost;
import com.tranv.workcv.entity.Category;
import com.tranv.workcv.entity.Company;
import com.tranv.workcv.entity.Recruitment;
import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.ApplyPostService;
import com.tranv.workcv.service.CategoryService;
import com.tranv.workcv.service.CompanyService;
import com.tranv.workcv.service.RecruitmentService;
import com.tranv.workcv.service.SaveJobService;
import com.tranv.workcv.service.UserService;
import com.tranv.workcv.until.Pagination;

@Controller
@RequestMapping("/recruitment")
public class RecruitmentController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	RecruitmentService recruitmentService;
	@Autowired
	private UserService userService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ApplyPostService applyPostService;
	@Autowired
	private SaveJobService saveJobService;

	// Retrieve the company associated with the currently authenticated user.
	private Company getCompanyByUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User theUser = userService.findByEmail(email);
		Company company = companyService.getCompanyByUserId(theUser.getId());
		return company;
	};

	// Retrieve the currently authenticated user.
	private User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User theUser = userService.findByEmail(email);
		return theUser;
	};

	// Handle the request to display the job post form.
	@GetMapping("/post")
	public String postJob(Model theModel) {
		List<Category> categories = categoryService.getCategories();
		theModel.addAttribute("categories", categories);
		return "public/post-job";
	}

	// Handle the request to display the list of job posts.
	@GetMapping("/list-post")
	public String showDonation(@RequestParam(name = "page", defaultValue = "1") int currentPage, Model theModel) {
		Company company = getCompanyByUser();
		List<Recruitment> recruitments = recruitmentService.getResultRecruitmentByCompany(company.getId());
		Pagination.pagination(recruitments, currentPage, theModel);
		return "public/post-list";
	}

	// Handle the request to add a new job post.
	@PostMapping("/add")
	public String addRecruitment(@ModelAttribute("recruitment") Recruitment newRecruitment) {
		Company company = getCompanyByUser();
		newRecruitment.setCompany(company);
		recruitmentService.saveRecruitment(newRecruitment);
		return "redirect:/recruitment/list-post";
	}

	// Handle the request to show the form for updating a job post.
	@GetMapping("/editpost")
	public String showFormForUpdate(@RequestParam("recruitmentId") int theId, Model theModel) {
		List<Category> categories = categoryService.getCategories();
		theModel.addAttribute("categories", categories);
		Recruitment recruitment = recruitmentService.getRecruitmentById(theId);
		theModel.addAttribute("recruitment", recruitment);
		return "public/edit-job";
	}

	// This method updates the provided recruitment object using the
	// recruitmentService,
	@PostMapping("/edit")
	public String updateRecruitment(@ModelAttribute("recruitment") Recruitment recruitment) {
		recruitmentService.update(recruitment);
		return "redirect:/recruitment/editpost?recruitmentId=" + recruitment.getId();
	}

	// Display the details of a job post.
	@GetMapping("/detail")
	public String detailJob(@RequestParam("recruitmentId") int recruitmentId, Model theModel) {
		Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);
		User theUser = getUser();
		if (theUser != null) {
			int userId = theUser.getId();
			boolean isSaveJob = theUser.getRecruitments().stream()
					.anyMatch(saveJob -> saveJob.getId() == recruitment.getId());
			if (isSaveJob) {
				saveJobService.unSaveJob(recruitmentId, userId);
			} else {
				saveJobService.saveJob(recruitmentId, userId);
			}
			theModel.addAttribute("isSaveJob", isSaveJob);
		}
		List<ApplyPost> listApplyPosts = applyPostService.listApplyPostByRecruitmentId(recruitmentId);
		theModel.addAttribute("applyPosts", listApplyPosts);
		theModel.addAttribute("recruitment", recruitment);
		return "public/detail-post";
	}

	// Delete a job post.
	@GetMapping("/delete")
	public String deleteRecruitment(@RequestParam("recruitmentId") int recruitmentId) {
		List<ApplyPost> applyPosts = applyPostService.listApplyPostByRecruitmentId(recruitmentId);
		System.out.println(applyPosts.size());
		for (ApplyPost applyPost : applyPosts) {
			applyPost.setRecruitment(null);
			applyPostService.saveOrUpdateApplyPost(applyPost);
		}
		recruitmentService.deleteRecruitment(recruitmentId);
		return "redirect:/recruitment/list-post";
	}

	// Search for job posts based on a search term.
	@GetMapping("/search")
	public String searchRecruitment(@RequestParam(name = "page", defaultValue = "1") int currentPage,
			@RequestParam("keySearch") String keySearch, Model theModel) {
		List<Recruitment> recruitments = recruitmentService.getResultRecruitment(keySearch);
		Pagination.pagination(recruitments, currentPage, theModel);
		theModel.addAttribute("keySearch", keySearch);
		return "public/result-search";
	}

	// Search for job posts based on an address search term.
	@GetMapping("/searchaddress")
	public String searchAdress(@RequestParam(name = "page", defaultValue = "1") int currentPage,
			@RequestParam("keySearch") String keySearch, Model theModel) {
		List<Recruitment> recruitments = recruitmentService.getResultAdress(keySearch);
		Pagination.pagination(recruitments, currentPage, theModel);
		theModel.addAttribute("keySearch", keySearch);
		return "public/result-search-address";
	}

	// Search for job posts based on a company search term.
	@GetMapping("/searchcompany")
	public String searchCompany(@RequestParam(name = "page", defaultValue = "1") int currentPage,
			@RequestParam("keySearch") String keySearch, Model theModel) {
		List<Recruitment> recruitments = recruitmentService.getResultCompany(keySearch);
		Pagination.pagination(recruitments, currentPage, theModel);
		theModel.addAttribute("keySearch", keySearch);
		return "public/result-search-company";
	}

	// Confirm an applied job post.
	@GetMapping("/approve")
	public String confirmPost(@RequestParam("applyPostId") int theId) {
		applyPostService.approve(theId);
		ApplyPost applyPost = applyPostService.getApplyPostbyId(theId);
		int recruitmentId = applyPost.getRecruitment().getId();
		return "redirect:/recruitment/detail?recruitmentId=" + recruitmentId;
	}

}
