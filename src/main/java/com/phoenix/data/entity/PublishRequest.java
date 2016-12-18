package com.phoenix.data.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="publish_request")
public class PublishRequest {
	
	@JsonIgnore
	@Id
	@Column(name="id")
	@SequenceGenerator(name="publishReqSeq", sequenceName="publish_request_squence", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="publishReqSeq")
	private int id;
	
	@Column(name="user_id")
	private int userId;
	
	@Column(name="description")
	@NotBlank(message="توضیحات نباید خالی باشد.")
	@Length(max=1000,message="توضیحات باید حداکثر شامل 1000 حرف باشد.")
	private String description;
	
	@JsonIgnore
	@Column(name="creation_date")
	private Timestamp creationDate;
	
	@JsonIgnore
	@Column(name="agreement")
	private boolean agreement;

	@JsonIgnore
	@Column(name="checked")
	private boolean checked;
	
	@JsonProperty
	public boolean isChecked() {
		return checked;
	}

	@JsonIgnore
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@JsonProperty
	public int getId() {
		return id;
	}
	
	@JsonIgnore
	public void setId(int id) {
		this.id = id;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty
	public Timestamp getCreationDate() {
		return creationDate;
	}

	@JsonIgnore
	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	@JsonProperty
	public boolean isAgreement() {
		return agreement;
	}
	
	@JsonIgnore
	public void setAgreement(boolean agreement) {
		this.agreement = agreement;
	}
}