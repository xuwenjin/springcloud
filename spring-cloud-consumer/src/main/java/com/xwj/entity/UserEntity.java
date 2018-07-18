package com.xwj.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity {

	private String id;

	private String lastName;

	private String email;

	private Integer age;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserEntity [id=" + id + ", lastName=" + lastName + ", email=" + email + ", age=" + age + "]";
	}

}
