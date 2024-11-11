package store.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import store.domain.Product;
import store.domain.Order;
import store.dto.ProductDto;
import store.loader.ProductLoaderImpl;
import store.repository.ProductRepository;
import store.repository.ProductRepositoryImpl;
import store.repository.OrderRepository;
import store.repository.OrderRepositoryImpl;

public class ProductServiceImpl implements ProductService {
	private static final ProductService INSTANCE;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	
	static {
		INSTANCE = new ProductServiceImpl(ProductRepositoryImpl.getInstance(),
										  OrderRepositoryImpl.getInstance());
	}
	
	private ProductServiceImpl(final ProductRepository productRepository,
							   final OrderRepository orderRepository) {
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
	}
	
	public static ProductService getInstance() {
		return INSTANCE;
	}
	
	@Override
	public List<ProductDto> getAll() {
		return productRepository.findAll().stream()
				.map(product -> ProductDto.of(product))
				.collect(Collectors.toList());
	}
	
	@Override
	public void purchase() {
		Order order = orderRepository.find();
		Map<Product, Long> payments = order.getPayments();
		payments.keySet().stream()
				.forEach(product -> calculate(product, payments));
	}
	
	@Override
	public void init() {
        productRepository.saveAll(ProductLoaderImpl.getInstance().load());
	}
	
	private void calculate(Product product, Map<Product, Long> payments) {
		List<Product> stocks = productRepository.findListInStockByName(product.getName());
		long totalPurchaseQuantity = payments.get(product);	// 전체 구매 수량
		
		if (hasPromotion(stocks.get(0))) {
			handlePromotion(stocks, totalPurchaseQuantity);
			return;
		}
		
		reduceQuantity(product, totalPurchaseQuantity);
	}
	
	private boolean hasPromotion(Product product) {
		if (product.getPromotion() == null) {
			return false;
		}
		
		return true;
	}
	
	private void handlePromotion(List<Product> stocks, long totalPurchaseQuantity) {
		long remain = totalPurchaseQuantity;	// 남은 구매 수량을 초기화
		
		for (Product stock : stocks) {
			long nowQuantity = stock.getQuantity(); // 현재 상품의 남은 수량
			
			if (isNowQuantityOverThanRemain(nowQuantity, remain, stock)) {
				break;
			}
			
			remain -= nowQuantity;
		}
	}
	
	private boolean isNowQuantityOverThanRemain(long nowQuantity, long remain, Product stock) {
		// 현재 재고가 남은 수량보다 많거나 같으면, 남은 수량만큼 구매
		if (nowQuantity >= remain) {
			reduceQuantity(stock, remain);
			return true;
		}
		
		// 현재 재고가 남은 수량보다 적으면, 현재 재고만큼 구매 후 남은 수량 갱신
		reduceQuantity(stock, nowQuantity);
		return false;
	}
	
	private void reduceQuantity(Product product, long quantity) {
		product.reduceQuantity(quantity);
		productRepository.save(product);
	}
}
