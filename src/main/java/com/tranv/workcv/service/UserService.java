package com.tranv.workcv.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.tranv.workcv.dto.RegisterDTO;
import com.tranv.workcv.entity.User;

public interface UserService extends UserDetailsService {

	
	public List<User> getListUsers();

	public User getUserById(int theId);

	public void saveUser(RegisterDTO theUser);

	public void update(User theUser);

	public void deleteUser(int theId);

	public User findByEmail(String email);

	User lockUser(int theId);
}
