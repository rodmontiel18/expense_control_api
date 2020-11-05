package com.rodmontiel.ec.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id",
		scope = User.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true, includeFieldNames = true)
public class User {

	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)
	private Date birthday;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Category> categories;

	@Column(name = "creation_date", nullable = false)
	private Date creationDate;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	private boolean enabled;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Expense> expenses;
	
	@Column(nullable = true)
	private char genre;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false, unique = true)
	private long id;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<Income> income;

	@Column(nullable = true)
	private String lastname;
	
	@Column(nullable = false)
	private String name;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(nullable = false)
	private String password;
	
	// private boolean tokenExpired;

	@Column(name = "update_date", nullable = false)
	private Date updateDate;

}
