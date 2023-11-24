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



import com.tranv.workcv.entity.Recruitment;
import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.ApplyPostService;

import com.tranv.workcv.service.RecruitmentService;
import com.tranv.workcv.service.SaveJobService;
import com.tranv.workcv.service.UserService;

@Controller
@RequestMapping("/job")
public class JobController {
	@Autowired
	private UserService userService;


	@Autowired
	private ApplyPostService applyPostService;

	@Autowired
	private SaveJobService saveJobService;

	@Autowired
	private RecruitmentService recruitmentService;

	

	// Retrieve the currently authenticated user.
	private User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User theUser = userService.findByEmail(email);
		return theUser;
	}

	

	// Handle the request to save a job.
	@PostMapping("/saveJob")
	public String saveJob(@RequestParam("recruitmentId") int recruitmentId) {
		User theUser = getUser();
		int userId = theUser.getId();

		Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);

		boolean isSaveJob = theUser.getRecruitments().stream()
				.anyMatch(saveJob -> saveJob.getId() == recruitment.getId());
		if (isSaveJob) {
			saveJobService.unSaveJob(recruitmentId, userId);
		} else {
			saveJobService.saveJob(recruitmentId, userId);
		}
		return "redirect:/";
	}
	@PostMapping("/saveJob2")
	public String saveJob2(@RequestParam("recruitmentId") int recruitmentId) {
		User theUser = getUser();
		int userId = theUser.getId();

		Recruitment recruitment = recruitmentService.getRecruitmentById(recruitmentId);

		boolean isSaveJob = theUser.getRecruitments().stream()
				.anyMatch(saveJob -> saveJob.getId() == recruitment.getId());
		if (isSaveJob) {
			saveJobService.unSaveJob(recruitmentId, userId);
		} else {
			saveJobService.saveJob(recruitmentId, userId);
		}
		return "redirect:/recruitment/detail?recruitmentId="+recruitmentId;
	}
	// Handle the request to unsave a job.
	@PostMapping("/unsaveJob")
	public String unSaveJob(@RequestParam("recruitmentId") int recruitmentId) {
		User theUser = getUser();
		int userId = theUser.getId();
		saveJobService.unSaveJob(recruitmentId, userId);
		return "redirect:/job/list-save-job";
	}

	// Handle the request to list saved jobs.
	@GetMapping("list-save-job")
	public String listSaveJob(@RequestParam(name = "page", defaultValue = "1") int currentPage, Model theModel) {
		User theUser = getUser();
		int theId = theUser.getId();
		List<Recruitment> recruitments = saveJobService.listSaveJobByUser(theId);
		int itemsPerPage = 5;
		int totalPages = (int) Math.ceil((double) recruitments.size() / itemsPerPage);
		int startIndex = (currentPage - 1) * itemsPerPage;
		List<Recruitment> currentPageDonations = recruitments.subList(startIndex,
				Math.min(startIndex + itemsPerPage, recruitments.size()));
		theModel.addAttribute("currentPage", currentPage);
		theModel.addAttribute("totalPages", totalPages);
		theModel.addAttribute("recruitments", currentPageDonations);
		return "list-save-job";
	}

	// Handle the request to delete a job.
	@GetMapping("/deleteJob")
	public String deleteJob(@RequestParam("applyPostId") int theId) {
		applyPostService.deleteJob(theId);
		return "redirect:/recruitment/list-apply-job";
	}
}
