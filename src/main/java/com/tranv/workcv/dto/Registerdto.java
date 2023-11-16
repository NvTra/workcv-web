package com.tranv.workcv.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Registerdto {
	private String email;
	private String fullName;
	private String password;
	private String rePassword;
	private int roleId;
	@Override
	public String toString() {
		return "Registerdto [email=" + email + ", fullName=" + fullName + ", password=" + password + ", rePassword="
				+ rePassword + ", roleId=" + roleId + "]";
	}
	
	
}
