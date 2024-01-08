package com.tranv.workcv.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranv.workcv.dao.UserDAO;
import com.tranv.workcv.dto.RegisterDTO;
import com.tranv.workcv.entity.ApplyPost;
import com.tranv.workcv.entity.Company;
import com.tranv.workcv.entity.User;
import com.tranv.workcv.service.ApplyPostService;
import com.tranv.workcv.service.CompanyService;
import com.tranv.workcv.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	// Service handles operations related to the User object
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ApplyPostService applyPostService;
	@Autowired
	private CompanyService companyService;

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
	public void saveUser(RegisterDTO theUser) {
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
		List<ApplyPost> applyPosts = applyPostService.listApplyPostsByUser(theId);
		for (ApplyPost applyPost : applyPosts) {
			applyPost.setUser(null);
		}
		Company company = companyService.getCompanyByUserId(theId);
		if (company != null) {
			company.setUser(null);
			company.setUsers(null);
		}
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

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDAO.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

//		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
//				mapRolesToAuthorities(user.getRole()));
	}

	/*
	 * private Collection<? extends GrantedAuthority>
	 * mapRolesToAuthorities(Collection<Role> roles) {
	 * 
	 * return roles.stream().map(role -> new
	 * SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList()); }
	 */

}
