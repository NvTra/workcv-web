package com.tranv.workcv.dao;

import java.util.List;

import com.tranv.workcv.entity.Cv;

public interface CvDAO {
	public List<Cv> getListCvs();

	public Cv getCvById(int theId);

	public void saveCv(Cv theCv);

	public void deleteCv(int theId);

	public Cv getCvByUserId(int theId);
}
