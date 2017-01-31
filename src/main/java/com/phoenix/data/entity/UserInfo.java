package com.phoenix.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="user_info")
public class UserInfo {

	@JsonIgnore
	@Id
	@Column(name="id")
	@SequenceGenerator(name="userSeq", sequenceName="user_squence", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="userSeq")
	private int id;
	
	@JsonIgnore
	@NotBlank(message="ایمیل نباید خالی باشد.")
	@Length(max=320, message="ایمیل باید دارای حداکثر 320 حرف باشد.")
	@Email(message="ایمیل معتبر نیست.")
	@Column(name="email", length=320)
	private String email;
	
	@JsonIgnore
	@NotBlank(message="رمز عبور نباید خالی باشد.")
	@Length(min=8, max=50, message="رمز عبور باید دارای حداقل هشت و حداکثر پنجاه حرف باشد.")
	@Column(name="password", length=50)
	private String password;
	
	@NotBlank(message="نام نمایشی نباید خالی باشد.")
	@Column(name="display_name", length=100)
	private String displayName;
	
	@JsonIgnore
	@Column(name="role")
	private String role;
	
	@JsonIgnore
	@Column(name="stroge_usage")
	private long strogeUsage;
	
	@JsonIgnore
	@Column(name="active")
	private boolean active;

	@JsonProperty
	public int getId() {
		return id;
	}

	@JsonIgnore
	public void setId(int id) {
		this.id = id;
	}

	@JsonIgnore
	public String getEmail() {
		return email;
	}

	@JsonProperty
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}
	
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@JsonProperty
	public long getStrogeUsage() {
		return strogeUsage;
	}

	@JsonIgnore
	public void setStrogeUsage(long strogeUsage) {
		this.strogeUsage = strogeUsage;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + id;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + (int) (strogeUsage ^ (strogeUsage >>> 32));
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserInfo other = (UserInfo) obj;
		if (active != other.active)
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (id != other.id)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (strogeUsage != other.strogeUsage)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", email=" + email + ", displayName=" + displayName + ", role=" + role
				+ ", strogeUsage=" + strogeUsage + ", active=" + active + "]";
	}	
}