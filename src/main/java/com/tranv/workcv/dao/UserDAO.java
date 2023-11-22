package com.tranv.workcv.dao;

import java.util.List;

import com.tranv.workcv.dto.RegisterDTO;
import com.tranv.workcv.entity.User;

public interface UserDAO {
	public List<User> getListUsers();

	public User getUserById(int theId);

	public void saveUser(RegisterDTO theUser);

	public void update(User theUser);

	public void deleteUser(int theId);

	public User findByEmail(String username);

	public User lockUser(int theId);
	
}
