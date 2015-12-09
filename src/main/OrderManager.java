package main;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {
	
	private List<Order> orders = new ArrayList<Order>();

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	
	public void addOrder(String message, String clientNumber) {
		Order order = new Order();
		order.setClientNumber(clientNumber);
		order.setContent(message.substring(6));
		orders.add(order);
	}
}
