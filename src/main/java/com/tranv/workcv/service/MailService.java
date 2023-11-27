package com.tranv.workcv.service;

import com.tranv.workcv.entity.User;

public interface MailService {
	public void sendEmailVerify(String to, User user);
}
