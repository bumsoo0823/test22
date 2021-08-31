package com.ibm.aiops.vo;

public class ResponseVo {

    private String userId;
    private String type;
    private String phoneNumber;
    private String lastName;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	@Override
	public String toString() {
		return "ResponseVo [userId=" + userId + ", type=" + type + ", phoneNumber=" + phoneNumber + ", lastName="
				+ lastName + "]";
	}
    
    

}
