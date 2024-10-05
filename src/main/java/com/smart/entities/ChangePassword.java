package com.smart.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePassword {
	@NotBlank(message = "can't be empty")
	private String old_password;
	@NotBlank(message="can't be empty")
	@Size(min = 4,message = "minimum 4 characters required")
	private String new_password;
	@NotBlank(message="can't be empty")
	private String confirm_password;
	public ChangePassword() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChangePassword(String old_password, String new_password, String confirm_password) {
		super();
		this.old_password = old_password;
		this.new_password = new_password;
		this.confirm_password = confirm_password;
	}
	public String getOld_password() {
		return old_password;
	}
	public void setOld_password(String old_password) {
		this.old_password = old_password;
	}
	public String getNew_password() {
		return new_password;
	}
	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}
	public String getConfirm_password() {
		return confirm_password;
	}
	public void setConfirm_password(String confirm_password) {
		this.confirm_password = confirm_password;
	}
	@Override
	public String toString() {
		return "ChangePassword [old_password=" + old_password + ", new_password=" + new_password + ", confirm_password="
				+ confirm_password + "]";
	}
	

}
