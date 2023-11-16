package com.tranv.workcv.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranv.workcv.dao.UserDAO;
import com.tranv.workcv.dto.Registerdto;
import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	// Service handles operations related to the User object
	@Autowired
	private UserDAO userDAO;

	// Method to get a list of all users from the database
	@Override
	@Transactional
	public List<User> getListUsers() {
		return userDAO.getListUsers();
	}

	// Method to get information about a user based on ID
	@Override
	@Transactional
	public User getUserById(int theId) {
		return userDAO.getUserById(theId);
	}

	// Method to save user to database
	@Override
	@Transactional
	public void saveUser(Registerdto theUser) {
		userDAO.saveUser(theUser);

	}

	// Method to update a user based on ID
	@Override
	@Transactional
	public void update(User theUser) {
		userDAO.update(theUser);

	}

	// Method to delete a user based on ID
	@Override
	@Transactional
	public void deleteUser(int theId) {
		userDAO.deleteUser(theId);

	}

	// Method to get information about a user based on email
	@Override
	@Transactional
	public User findByEmail(String email) {

		return userDAO.findByEmail(email);
	}

	// Method to unlock a user based on ID
	@Override
	@Transactional
	public User lockUser(int theId) {
		return userDAO.lockUser(theId);
	}

}
