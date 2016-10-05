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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="user_info")
public class Publisher
{
	@Id
	@Column(name="id")
	@SequenceGenerator(name="publisherSeq", sequenceName="user_squence", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="publisherSeq")
	private int id;
	
	@Column(name="username", length=50)
	private String username;
	
	@Column(name="display_name", length=100)
	private String displayName;
	
	@Column(name="role")
	private String role;
	
	@OneToMany
	@JoinColumn(name="publisher_id", referencedColumnName="id")
	@Fetch(FetchMode.JOIN)
	private List<BoardInfo> myBoards;
	
	@ManyToMany
	@JoinTable(name="subscribed_board",
		joinColumns={@JoinColumn(name="subscriber_id")},
		inverseJoinColumns={@JoinColumn(name="board_id")})
	@Fetch(FetchMode.JOIN)
	private List<Board> subscribedBoards;
	
	@ManyToMany
	@JoinTable(name="post_notification",
		joinColumns={@JoinColumn(name="subscriber_id")},
		inverseJoinColumns={@JoinColumn(name="post_id")})
	@Fetch(FetchMode.JOIN)
	private List<BoardPost> newPosts;

	@Column(name="stroge_usage")
	private long strogeUsage;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public List<BoardInfo> getMyBoards() {
		return myBoards;
	}

	public void setMyBoards(List<BoardInfo> myBoards) {
		this.myBoards = myBoards;
	}

	public List<Board> getSubscribedBoards() {
		return subscribedBoards;
	}

	public void setSubscribedBoards(List<Board> subscribedBoards) {
		this.subscribedBoards = subscribedBoards;
	}

	public List<BoardPost> getNewPosts() {
		return newPosts;
	}

	public void setNewPosts(List<BoardPost> newPosts) {
		this.newPosts = newPosts;
	}

	public long getStrogeUsage() {
		return strogeUsage;
	}

	public void setStrogeUsage(long strogeUsage) {
		this.strogeUsage = strogeUsage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + id;
		result = prime * result + ((myBoards == null) ? 0 : myBoards.hashCode());
		result = prime * result + ((newPosts == null) ? 0 : newPosts.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + (int) (strogeUsage ^ (strogeUsage >>> 32));
		result = prime * result + ((subscribedBoards == null) ? 0 : subscribedBoards.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		Publisher other = (Publisher) obj;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (id != other.id)
			return false;
		if (myBoards == null) {
			if (other.myBoards != null)
				return false;
		} else if (!myBoards.equals(other.myBoards))
			return false;
		if (newPosts == null) {
			if (other.newPosts != null)
				return false;
		} else if (!newPosts.equals(other.newPosts))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (strogeUsage != other.strogeUsage)
			return false;
		if (subscribedBoards == null) {
			if (other.subscribedBoards != null)
				return false;
		} else if (!subscribedBoards.equals(other.subscribedBoards))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Publisher [id=" + id + ", username=" + username + ", displayName=" + displayName + ", role=" + role
				+ ", myBoards=" + myBoards + ", subscribedBoards=" + subscribedBoards + ", newPosts=" + newPosts
				+ ", strogeUsage=" + strogeUsage + "]";
	}

		
}