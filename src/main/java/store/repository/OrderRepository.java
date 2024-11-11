package store.repository;

import store.domain.Product;
import store.domain.Order;

public interface OrderRepository {
	Order save(Order order);
	Order find();
	boolean addPayment(Product product, long quantity);
	boolean addPresent(Product product, long quantity);
	Order membershipDiscount(Order order);
	void clear();
}
