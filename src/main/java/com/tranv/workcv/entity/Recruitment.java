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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recruitment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recruitment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "title")
	private String title;

	@Column(name = "type")
	private String type;

	@Column(name = "view")
	private String view;

	@Column(name = "address")
	private String address;

	@Column(name = "created_at")
	private String createdAt;

	@Column(name = "description")
	private String description;

	@Column(name = "experience")
	private String experience;

	@Column(name = "quantity")
	private String quantity;

	@Column(name = "ranked")
	private String ranked;

	@Column(name = "salary")
	private String salary;

	@Column(name = "deadline")
	private String deadline;

	@Column(name = "status")
	private int status;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "company_id")
	private Company company;

	@OneToMany(mappedBy = "recruitment", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
			CascadeType.REFRESH })
	private List<ApplyPost> applyPosts;
	
	
	@ManyToMany(mappedBy = "recruitments")
	private Set<User> users = new HashSet<User>();

	
}
