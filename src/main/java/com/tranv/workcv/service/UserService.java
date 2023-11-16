package com.tranv.workcv.service;

import java.util.List;

import com.tranv.workcv.dto.Registerdto;
import com.tranv.workcv.entity.User;

public interface UserService {
	public List<User> getListUsers();

	public User getUserById(int theId);

	public void saveUser(Registerdto theUser);

	public void update(User theUser);

	public void deleteUser(int theId);

	public User findByEmail(String email);

	User lockUser(int theId);
}
