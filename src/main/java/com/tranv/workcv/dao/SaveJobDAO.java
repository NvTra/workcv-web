package com.tranv.workcv.dao;

import java.util.List;

import com.tranv.workcv.entity.Recruitment;

public interface SaveJobDAO {
	public void saveJob(int recruitmentId, int userId);

	public void unSaveJob(int recruitmentId, int userId);

	public List<Recruitment> listSaveJobByUser(int theId);
}
