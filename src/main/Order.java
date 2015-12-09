package main;

import java.util.Date;

public class Order {
	
	private String clientNumber;
	
	private Date dateOfOrder;
	
	private OrderStatus status;
	
	private String content;
	
	public Order() {
		status = OrderStatus.INIT;
		dateOfOrder = new Date();
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}

	public Date getDateOfOrder() {
		return dateOfOrder;
	}

	public void setDateOfOrder(Date dateOfOrder) {
		this.dateOfOrder = dateOfOrder;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "(" + clientNumber + ")" +content;
	}

}
