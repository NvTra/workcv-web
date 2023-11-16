package com.tranv.workcv.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tranv.workcv.entity.Category;
import com.tranv.workcv.entity.Company;
import com.tranv.workcv.entity.Cv;
import com.tranv.workcv.entity.Recruitment;
import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.CategoryService;
import com.tranv.workcv.service.CompanyService;
import com.tranv.workcv.service.CvService;
import com.tranv.workcv.service.RecruitmentService;
import com.tranv.workcv.service.UserService;
import com.tranv.workcv.until.ImageUtil;

@Controller

public class HomeController {
	@Autowired
	private UserService userService;

	@Autowired
	private CompanyService companyService;
	@Autowired
	private RecruitmentService recruitmentService;
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CvService cvService;

	// Handle the request to show the home page.
	@GetMapping("/")
	public String showHome(HttpServletRequest request, Model theModel) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User theUser = userService.findByEmail(email);
		theModel.addAttribute("theUser", theUser);
		if (theUser != null) {
			Cv cv = cvService.getCvByUserId(theUser.getId());
			theModel.addAttribute("userCv", cv);
		}
		request.setAttribute("msg_register_success", "done");
		List<Category> categories = categoryService.getTop4Categorys();
		theModel.addAttribute("categories", categories);
		List<Recruitment> recruitments = recruitmentService.getResultRecruitmentBySalary();
		theModel.addAttribute("recruitments", recruitments);
		List<Company> companies = companyService.getCompanyTop();
		theModel.addAttribute("imgUtil", new ImageUtil());
		theModel.addAttribute("companies", companies);
		return "/public/home";
	}

	// Handle the request to show the user profile page.
	@GetMapping("/detail")
	public String detail(Model theModel) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		User theUser = userService.findByEmail(email);
		int userId = theUser.getId();
		Company theCompany = companyService.getCompanyByUserId(userId);
		Cv theCv = cvService.getCvByUserId(userId);
		theModel.addAttribute("Cv", theCv);
		theModel.addAttribute("user", theUser);
		theModel.addAttribute("company", theCompany);
		return "profile";
	}

}
