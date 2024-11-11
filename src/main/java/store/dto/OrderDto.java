package store.dto;

import java.util.Map;

import store.domain.Order;
import store.domain.Product;

public class OrderDto {
	private final Map<Product, Long> payments;
	private final Map<Product, Long> presents;
	private final boolean membershipDiscount;
	
	private OrderDto(final Order order) {
		this.payments = order.getPayments();
		this.presents = order.getPresents();
		this.membershipDiscount = order.isMembershipDiscount();
	}
	
	public static OrderDto of(final Order order) {
		return new OrderDto(order);
	}

	public Map<Product, Long> getPayments() {
		return payments;
	}

	public Map<Product, Long> getPresents() {
		return presents;
	}

	public boolean isMembershipDiscount() {
		return membershipDiscount;
	}
}
