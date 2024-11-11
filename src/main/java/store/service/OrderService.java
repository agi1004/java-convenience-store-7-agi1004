package store.service;

import java.util.List;

import store.dto.OrderDto;

public interface OrderService {
	OrderDto order(List<String> items);
	OrderDto membershipDiscount();
	void reset();
}
