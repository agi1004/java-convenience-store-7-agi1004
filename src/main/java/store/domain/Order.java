package store.domain;

import java.util.HashMap;
import java.util.Map;

import store.dto.OrderDto;

public class Order {
	private Map<Product, Long> payments;
	private Map<Product, Long> presents;
	private boolean membershipDiscount;
	
	private Order(final Map<Product, Long> payments,
				  final Map<Product, Long> presents,
				  final boolean membershipDiscount) {
		this.payments = payments;
		this.presents = presents;
		this.membershipDiscount = membershipDiscount;
	}
	
	public static Order newInstance() {
		return new Order(new HashMap<>(), new HashMap<>(), false);
	}
	
	public static Order of(OrderDto orderDto) {
		return new Order(orderDto.getPayments(), orderDto.getPresents(), 
						 orderDto.isMembershipDiscount());
	}
	
	public void membershipDiscount() {
		this.membershipDiscount = true;
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
