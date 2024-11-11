package store.repository;

import store.domain.Product;
import store.domain.Order;

public class OrderRepositoryImpl implements OrderRepository {
	private static final OrderRepository INSTANCE;
	private Order order;
	
	static {
		INSTANCE = new OrderRepositoryImpl();
	}
	
	private OrderRepositoryImpl() {
		order = Order.newInstance();
	}
	
	public static OrderRepository getInstance() {
		return INSTANCE;
	}
	
	@Override
	public Order save(Order order) {
		this.order = order;
		return order;
	}

	@Override
	public Order find() {
		return order;
	}
	
	@Override
	public boolean addPayment(Product product, long quantity) {
		order.getPayments().put(product, quantity);
		return true;
	}
	
	@Override
	public boolean addPresent(Product product, long quantity) {
		order.getPresents().put(product, quantity);
		return true;
	}
	
	@Override
	public Order membershipDiscount(Order order) {
		order.membershipDiscount();
		return order;
	}
	
	@Override
	public void clear() {
		order = Order.newInstance();
	}
}
