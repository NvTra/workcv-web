package com.tranv.workcv.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {
	private String email;
	private String fullName;
	private String password;

	private String rePassword;
	private int roleId;

	public boolean passwordsMatch() {
		return password.equals(rePassword);
	}
}
