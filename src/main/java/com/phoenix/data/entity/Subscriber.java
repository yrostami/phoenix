package com.phoenix.data.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="user_info")
public class Subscriber
{
	@Id
	@Column(name="id")
	@SequenceGenerator(name="subscriberSeq", sequenceName="user_squence", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="subscriberSeq")
	private int id;
	
	@Column(name="email", length=50)
	private String email;
	
	@Column(name="display_name", length=100)
	private String displayName;
	
	@Column(name="role")
	private String role;
	
	@ManyToMany
	@JoinTable(name="subscribed_board",
		joinColumns={@JoinColumn(name="subscriber_id")},
		inverseJoinColumns={@JoinColumn(name="board_id")})
	@Fetch(FetchMode.JOIN)
	private List<Board> subscribedBoards;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public List<Board> getSubscribedBoards() {
		return subscribedBoards;
	}

	public void setSubscribedBoards(List<Board> subscribedBoards) {
		this.subscribedBoards = subscribedBoards;
	}

//	public List<BoardPost> getNewPosts() {
//		return newPosts;
//	}
//
//	public void setNewPosts(List<BoardPost> newPosts) {
//		this.newPosts = newPosts;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + id;
//		result = prime * result + ((newPosts == null) ? 0 : newPosts.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((subscribedBoards == null) ? 0 : subscribedBoards.hashCode());
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
		Subscriber other = (Subscriber) obj;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (id != other.id)
			return false;
//		if (newPosts == null) {
//			if (other.newPosts != null)
//				return false;
//		} else if (!newPosts.equals(other.newPosts))
//			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (subscribedBoards == null) {
			if (other.subscribedBoards != null)
				return false;
		} else if (!subscribedBoards.equals(other.subscribedBoards))
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
		return "Subscriber [id=" + id + ", email=" + email + ", displayName=" + displayName + ", role=" + role
				+ ", subscribedBoards=" + subscribedBoards + "]";
	}
}