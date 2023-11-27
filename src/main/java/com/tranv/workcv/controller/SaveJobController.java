package com.tranv.workcv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tranv.workcv.entity.Recruitment;
import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.RecruitmentService;
import com.tranv.workcv.service.SaveJobService;
import com.tranv.workcv.service.UserService;
import com.tranv.workcv.until.PaginationUtil;

@Controller
@RequestMapping("/save-job")
public class SaveJobController {
	@Autowired
	private SaveJobService saveJobService;
	@Autowired
	private RecruitmentService recruitmentService;
	@Autowired
	private UserService userService;

	private User getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User theUser = userService.findByEmail(email);
		return theUser;
	};

	@PostMapping("/save")
	public @ResponseBody String save(@RequestParam("idRe") int idRe) {

		User theUser = getUser();

		if (theUser == null) {
			return "false";
		} else {
			Recruitment recruitment = recruitmentService.getRecruitmentById(idRe);

			boolean isSaveJob = theUser.getRecruitments().stream()
					.anyMatch(saveJob -> saveJob.getId() == recruitment.getId());
			if (isSaveJob) {
//			saveJobService.unSaveJob(idRe, userId);
				return "error";
			} else {
				saveJobService.saveJob(idRe, theUser.getId());
				return "true";
			}
		}
	}

	@GetMapping("/delete/{idRe}")
	public String delete(@PathVariable("idRe") int idRe) {
		User user = getUser();

		boolean isSaveJob = user.getRecruitments().stream().anyMatch(saveJob -> saveJob.getId() == idRe);
		if (isSaveJob) {
			saveJobService.unSaveJob(idRe, user.getId());
		}
		return "redirect:/save-job/get-list";
	}

	@GetMapping("/get-list")
	public String listSaveJob(@RequestParam(name = "page", defaultValue = "1") int currentPage, Model model) {
		User theUser = getUser();
		int theId = theUser.getId();
		List<Recruitment> recruitments = saveJobService.listSaveJobByUser(theId);
		PaginationUtil.pagination(recruitments, currentPage, model);
		return "public/list-save-job";
	}

}
