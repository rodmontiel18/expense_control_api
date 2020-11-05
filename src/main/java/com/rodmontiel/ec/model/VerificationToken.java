package com.rodmontiel.ec.model;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
public class VerificationToken {

  public VerificationToken(User user) {
    this.user = user;
    createdDate = new Date();
    token = UUID.randomUUID().toString();
    Calendar cal = Calendar.getInstance();
    cal.setTime(createdDate);
    cal.add(Calendar.DAY_OF_MONTH, 1);
    expirationDate = cal.getTime();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String token;
  
  @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  private Date createdDate;

  private Date expirationDate;
}
