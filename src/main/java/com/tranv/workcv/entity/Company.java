package com.tranv.workcv.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "company")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name_company")
	private String nameCompany;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "email")
	private String email;

	@Column(name = "address")
	private String address;

	@Column(name = "description")
	private String description;

	@Lob
	@Column(name = "logo", columnDefinition = "MEDIUMBLOB")
	private byte[] logo;

	@Column(name = "status")
	private int status;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "company", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	private List<Recruitment> recruitments;

	@ManyToMany(mappedBy = "companies")
	private Set<User> users = new HashSet<User>();
	


}