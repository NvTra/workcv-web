package com.tranv.workcv.service;

import java.util.List;

import com.tranv.workcv.entity.Recruitment;



public interface SaveJobService {
	public void saveJob(int recruitmentId, int userId);

	public void unSaveJob(int recruitmentId, int userId);
	
	public List<Recruitment> listSaveJobByUser(int theId);
}
