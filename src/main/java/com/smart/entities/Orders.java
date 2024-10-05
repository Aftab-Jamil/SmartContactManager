package com.smart.entities;

import org.antlr.v4.runtime.tree.pattern.ParseTreePatternMatcher.StartRuleDoesNotConsumeFullPattern;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int myId;
	private String orderId;
	private String paymentId;
	private int amount;
	private String status;
	@ManyToOne
	private User user;

	public int getMyId() {
		return myId;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public Orders() {
		super();
		// TODO Auto-generated constructor stub
	}

public Orders(String orderId, String paymentId, int amount, String status, User user) {
	super();
	this.orderId = orderId;
	this.paymentId = paymentId;
	this.amount = amount;
	this.status = status;
	this.user = user;
}

}
