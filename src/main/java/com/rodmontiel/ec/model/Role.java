package com.rodmontiel.ec.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.JoinTable;
//import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Entity
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id",
		scope = Role.class)
@Data
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id", nullable = false, unique = true)
    private Long id;
 
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;
 
    /*
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "role_privilege", 
        joinColumns = @JoinColumn(
          name = "role_id"), 
        inverseJoinColumns = @JoinColumn(
          name = "privilege_id"))
    private Collection<Privilege> privileges;
    */
    
}
