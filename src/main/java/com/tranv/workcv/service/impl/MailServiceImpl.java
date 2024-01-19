package com.tranv.workcv.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.MailService;

@Service
public class MailServiceImpl implements MailService {
	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void sendEmailVerify(String to, User user) {

		String link = "http://localhost:8080/spring-workcv/user/confirm-account";
		String text = "Vui lòng ấn vào link sau để xác minh tài khoản: /n " + link;

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject("Xác minh tài khoản");
		mailMessage.setText(text);
		javaMailSender.send(mailMessage);
	}

}
