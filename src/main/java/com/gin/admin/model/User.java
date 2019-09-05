package com.gin.admin.model;

import java.io.Serializable;

import com.gin.admin.dao.annotation.Column;
import com.gin.admin.dao.annotation.Id;
import com.gin.admin.dao.annotation.Table;

/**
 * 用户
 *
 * @author gin
 * @since 2019-07-03
 */
@Table
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	private Integer id;
	@Column
	private String userName;
	@Column
	private String password;
	@Column
	private String nickName;
	@Column
	private String qqNo;
	@Column
	private String wxNo;
	@Column
	private String mobile;
	@Column
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
