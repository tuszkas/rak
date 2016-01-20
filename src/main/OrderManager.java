package main;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {
	
	private List<Order> orders = new ArrayList<>();

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
	
	public void updateOrderStatus(OrderStatus newStatus, int orderIndex) {
		orders.get(orderIndex).setStatus(newStatus);
	}
	
	public Order getOrder(int index) {
		return orders.get(index);
	}
	
	public void deleteOrder(int index) {
		orders.remove(index);
	}

	public Order findOrderWihNumber(String clientNumber) {
		for (Order o : orders) {
			if (o.getClientNumber().equals(clientNumber)) {
				return o;
			}
		}
		return null;
	}

	public String updateOrderStatus(OrderStatus orderStatus, String clientNumber) {
		Order order = findOrderWihNumber(clientNumber);
		if (order!= null) {
			order.setStatus(orderStatus);
			return "Zaktualizowano status";
		} else {
			return "Nie znaleziono zamowienia dla tego numeru";
		}
	}

	public String removeOrder( String clientNumber) {
		Order order = findOrderWihNumber(clientNumber);
		if (!order.getStatus().equals(OrderStatus.SENT)) {
			orders.remove(order);
			return "Zamowienie anulowane";
		} else {
			return "Zamowienie jest juz w drodze, nie moze zostac anulowane";
		}
	}
}
