package com.tranv.workcv.until;

import java.util.List;

import org.springframework.ui.Model;


public class Pagination {
	public static <T> void pagination(List<T> list, int currentPage, Model theModel) {
		int itemsPerPage = 5;
		int totalPages = (int) Math.ceil((double) list.size() / itemsPerPage);
		int startIndex = (currentPage - 1) * itemsPerPage;
		List<T> currentListPage = list.subList(startIndex,
				Math.min(startIndex + itemsPerPage, list.size()));
		theModel.addAttribute("currentPage", currentPage);
		theModel.addAttribute("totalPages", totalPages);
		theModel.addAttribute("list", currentListPage);
	}
}
