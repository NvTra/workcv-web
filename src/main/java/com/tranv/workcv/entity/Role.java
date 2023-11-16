package com.tranv.workcv.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="role")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "role_name")
	private String roleName;

	@OneToMany(mappedBy = "role", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
			CascadeType.REFRESH })
	private List<User> users;

	// method
	public void add(User tempUser) {
		if (users == null) {
			users = new ArrayList<>();
		}
		users.add(tempUser);
		tempUser.setRole(this);
	}
	
}
