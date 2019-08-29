package com.gin.admin.model;

import java.io.Serializable;

import org.springframework.data.annotation.Transient;

/**
 * 用户
 *
 * @author gin
 * @since 2019-07-03
 */
public class User implements Serializable {
	@Transient
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String userName;
	private String password;
	private String nickName;
	private String qqNo;
	private String wxNo;
	private String mobile;
	private String email;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getQqNo() {
		return qqNo;
	}

	public void setQqNo(String qqNo) {
		this.qqNo = qqNo;
	}

	public String getWxNo() {
		return wxNo;
	}

	public void setWxNo(String wxNo) {
		this.wxNo = wxNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", nickName=" + nickName
				+ ", qqNo=" + qqNo + ", wxNo=" + wxNo + ", mobile=" + mobile + ", email=" + email + "]";
	}
}
