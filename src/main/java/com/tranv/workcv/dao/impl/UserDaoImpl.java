package com.tranv.workcv.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.tranv.workcv.dao.UserDAO;
import com.tranv.workcv.dto.RegisterDTO;
import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.RoleService;

@Repository
public class UserDaoImpl implements UserDAO {

	// DAO handles operations related to the User object
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private RoleService roleService;

	// Method to get a list of all users from the database
	@Override
	public List<User> getListUsers() {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<User> theQuery = currentSession.createQuery("from User", User.class);
		List<User> users = theQuery.getResultList();
		return users;
	}

	// Method to get information about a user based on ID
	@Override
	public User getUserById(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		User theUser = currentSession.get(User.class, theId);
		return theUser;
	}

	// Method to save user to database
	@Override
	public void saveUser(RegisterDTO theUser) {

		Session currentSession = sessionFactory.getCurrentSession();
		User newUser = convertToEntity(theUser);
		newUser.setStatus(1);
		newUser.setActive(true);
		currentSession.save(newUser);

	}

	// Method to update a user based on ID
	@Override
	public void update(User theUser) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.update(theUser);

	}

	// Method to delete a user based on ID
	@Override
	public void deleteUser(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<User> theQuery = currentSession.createQuery("delete from User where id:=userId", User.class);
		theQuery.setParameter("userId", theId);
		theQuery.executeUpdate();

	}

	// Method to get information about a user based on email
	@Override
	public User findByEmail(String email) {
		Session currentSession = sessionFactory.getCurrentSession();
		Query<User> theQuery = currentSession.createQuery("from User where email = :email", User.class);
		theQuery.setParameter("email", email);
		User user = theQuery.getSingleResult();
		return user;
	}

	// Method to unlock a user based on ID
	@Override
	public User lockUser(int theId) {
		Session currentSession = sessionFactory.getCurrentSession();
		User theUser = currentSession.get(User.class, theId);
		theUser.setStatus(1);
		currentSession.update(theUser);
		return theUser;
	}

	private User convertToEntity(RegisterDTO dto) {
		User newUser = new User();
		newUser.setEmail(dto.getEmail());
		newUser.setFullName(dto.getFullName());
		newUser.setPassword(passwordEncoder().encode(dto.getPassword()));
		newUser.setRole(roleService.getRolebyRoleId(dto.getRoleId()));
		return newUser;
	}

	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
