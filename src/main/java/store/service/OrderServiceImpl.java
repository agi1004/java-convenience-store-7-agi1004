package store.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import store.domain.Product;
import store.domain.Promotion;
import store.dto.OrderDto;
import store.domain.Order;
import store.repository.ProductRepository;
import store.repository.ProductRepositoryImpl;
import store.view.InputView;
import store.view.InputViewImpl;
import store.view.OutputView;
import store.view.OutputViewImpl;
import store.repository.OrderRepository;
import store.repository.OrderRepositoryImpl;

import static store.constant.ExceptionMessage.NON_EXISTENT_PRODUCT;
import static store.constant.ExceptionMessage.EXCEEDED_QUANTITY;
import static store.constant.ExceptionMessage.OUT_OF_STOCK;
import static store.constant.PrintMessage.NOW;
import static store.constant.PrintMessage.SPACE;
import static store.constant.PrintMessage.NON_APPLICABLE_PROMOTIONAL_DISCOUNT;
import static store.constant.PrintMessage.SUBJECT;
import static store.constant.PrintMessage.FREE_OF_CHARGE;
import static store.constant.Regex.ITEM_SEPARATOR;
import static store.constant.Number.BOTH_END_INDEXES;
import static store.constant.Number.FIRST_PRODUCT_INDEX;
import static store.constant.Number.ZERO;
import static store.enums.PurchaseItemIndex.NAME;
import static store.enums.PurchaseItemIndex.QUANTITY;

public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final InputView inputView;
	private final OutputView outputView;
	
	private OrderServiceImpl(final OrderRepository orderRepository,
							 final ProductRepository productRepository,
							 final InputView inputView,
							 final OutputView outputView) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
		this.inputView = inputView;
		this.outputView = outputView;
	}
	
	public static OrderService getInstance() {
		return new OrderServiceImpl(OrderRepositoryImpl.getInstance(),
								  	ProductRepositoryImpl.getInstance(),
								  	InputViewImpl.getInstance(),
								  	OutputViewImpl.getInstance());
	}
	
	@Override
	public OrderDto order(List<String> items) {
		items.stream().forEach(item -> validateProduct(getProductName(item)));
		items.stream().forEach(item -> processItem(item));
		Order order = orderRepository.find();
		return OrderDto.of(orderRepository.save(order));
	}
	
	@Override
	public OrderDto membershipDiscount() {
		Order order = orderRepository.find();
		Order newOrder = orderRepository.membershipDiscount(order);
		return OrderDto.of(orderRepository.save(newOrder));
	}
	
	@Override
	public void reset() {
		orderRepository.clear();
	}
	
	private void validateProduct(String productName) {
		productRepository.findByName(productName)
			.orElseThrow(() -> new IllegalArgumentException(NON_EXISTENT_PRODUCT));
	}
	
	private void processItem(String item) {
		String productName = getProductName(item);
		Product product = getListInStockByName(productName).get(FIRST_PRODUCT_INDEX);
		long purchaseQuantity = getPurchaseQuantity(item);
		long totalQuantity = getTotalQuantity(productName);
		
		validatePurchaseQuantity(purchaseQuantity, totalQuantity);
		coveredOrNonCoveredPromotion(product, purchaseQuantity, totalQuantity);
	}
	
	private List<Product> getListInStockByName(String productName) {
		List<Product> stocks = productRepository.findListInStockByName(productName);
		
		if (stocks.isEmpty()) {
			throw new IllegalArgumentException(OUT_OF_STOCK);
		}
		
		return stocks;
	}
	
	private long getTotalQuantity(String productName) {
		return getListInStockByName(productName).stream()
				.mapToLong(Product::getQuantity)
				.sum();
	}
	
	private String[] parseItem(String item) {
		item = item.substring(BOTH_END_INDEXES, item.length() - BOTH_END_INDEXES);
		return item.split(ITEM_SEPARATOR);
	}
	
	private String getProductName(String item) {
		String[] purchaseItem = parseItem(item);
		return purchaseItem[NAME.getValue()];
	}
	
	private long getPurchaseQuantity(String item) {
		String[] purchaseItem = parseItem(item);
		return Long.parseLong(purchaseItem[QUANTITY.getValue()]);
	}
	
	private void validatePurchaseQuantity(long purchaseQuantity, long totalQuantity) {
		if (purchaseQuantity > totalQuantity) {
			throw new IllegalArgumentException(EXCEEDED_QUANTITY);
		}
	}
	
	private void coveredOrNonCoveredPromotion(Product product, long purchaseQuantity, long totalQuantity) {
		if (hasPromotion(product)) {
			checkPromotionDate(product, purchaseQuantity, totalQuantity);
			return;
		}
		
		nonCoveredPromotion(product, purchaseQuantity, totalQuantity);
	}
	
	private boolean hasPromotion(Product product) {
		if (product.getPromotion() == null) {
			return false;
		}
		
		return true;
	}
	
	private void checkPromotionDate(Product product, long purchaseQuantity, long totalQuantity) {
		if (isValidPromotionDate(product.getPromotion())) {
			coveredPromotion(product, purchaseQuantity, totalQuantity);
			return;
		}
		
		nonCoveredPromotion(product, purchaseQuantity, totalQuantity);
	}
	
	private boolean isValidPromotionDate(Promotion promotion) {
		LocalDate now = LocalDate.now(getClock());
		
		LocalDate startDate = promotion.getStartDate();
		LocalDate endDate = promotion.getEndDate();
		return !now.isAfter(endDate) && !now.isBefore(startDate);
	}
	
	private void coveredPromotion(Product product, long purchaseQuantity, long totalQuantity) {
		Promotion promotion = product.getPromotion();
		long promotionQuantity = product.getQuantity();	// 프로모션 개수
		int promotionSet = promotion.getBuy() + promotion.getGet();	// 프로모션 세트
		
		if (purchaseQuantity > promotionQuantity) {
			overThanPromotionQuantity(product, purchaseQuantity, promotionQuantity, promotionSet);
			return;
		}
		
		lessThanPromotionQuantity(product, purchaseQuantity, promotionQuantity, promotionSet, promotion.getGet(), totalQuantity);
	}
	
	private void overThanPromotionQuantity(Product product, long purchaseQuantity, long promotionQuantity, int promotionSet) {
		long purchaseSet = getPurchaseSet(promotionQuantity, promotionSet);	// 만들 수 있는 세트
		long coveredPromotionQuantity = getCoveredPromotionQuantity(promotionSet, purchaseSet);		// 프로모션 적용 O 개수
		long nonCoveredPromotionQuantity = purchaseQuantity - coveredPromotionQuantity;	// 프로모션 적용 X 개수
		
		if (askForNonCoveredPromotionQuantity(product.getName(), nonCoveredPromotionQuantity)) {
			addProduct(product, purchaseQuantity, purchaseSet);
			return;
		}
		
		addProduct(product, coveredPromotionQuantity, purchaseSet);
	}
	
	private long getPurchaseSet(long purchaseQuantity, long promotionSet) {
		return purchaseQuantity / promotionSet;
	}
	
	private long getCoveredPromotionQuantity(long promotionSet, long purchaseSet) {
		return promotionSet * purchaseSet;
	}
	
	private boolean askForNonCoveredPromotionQuantity(String productName, long nonCoveredPromotionQuantity) {
		System.out.println(NOW + productName + SPACE + nonCoveredPromotionQuantity + NON_APPLICABLE_PROMOTIONAL_DISCOUNT);
		
		while (true) {
			try {
				return inputView.askForNonCoveredPromotionQuantity(productName, nonCoveredPromotionQuantity);
			} catch (IllegalArgumentException e) {
				outputView.printErrorMessage(e.getMessage());
			}
		}
	}
	
	private void lessThanPromotionQuantity(Product product, long purchaseQuantity, long promotionQuantity, int promotionSet, int get, long totalQuantity) {
		long purchaseSet = getPurchaseSet(purchaseQuantity, promotionSet);	// 만들 수 있는 세트
		long coveredPromotionQuantity = getCoveredPromotionQuantity(promotionSet, purchaseSet);		// 프로모션 적용 O 개수
		
		if (processPromotionSetConditions(product, purchaseQuantity, promotionSet, purchaseSet)) {
			return;
		}
		
		if (purchaseQuantity > coveredPromotionQuantity) {
			overThanCoveredPromotionQuantity(product, purchaseQuantity, purchaseSet, promotionSet);	
		}
	}
	
	private boolean processPromotionSetConditions(Product product, long purchaseQuantity, int promotionSet, long purchaseSet) {
		if (purchaseQuantity % promotionSet == ZERO) {
			return addProduct(product, purchaseQuantity, purchaseSet);
		}
		
		if (purchaseQuantity < promotionSet) {
			return lessThanPromotionSet(product, purchaseQuantity, promotionSet);
		}
		
		return false;
	}
	
	private boolean lessThanPromotionSet(Product product, long purchaseQuantity, long promotionSet) {
		long lackQuantity = getLackQuantity(promotionSet, purchaseQuantity);
		
		if (canGetForFree(product, purchaseQuantity, promotionSet, lackQuantity)) {
			return addProduct(product, purchaseQuantity + lackQuantity, lackQuantity);
		}
		
		return orderRepository.addPayment(product, purchaseQuantity);
	}
	
	private void overThanCoveredPromotionQuantity(Product product, long purchaseQuantity, long purchaseSet, long promotionSet) {
		long lackQuantity = getLackQuantity(promotionSet, purchaseQuantity % promotionSet);
		
		if (canGetForFree(product, purchaseQuantity, promotionSet, lackQuantity)) {
			addProduct(product, purchaseQuantity + lackQuantity, purchaseSet + lackQuantity);
			return;
		}
		
		addProduct(product, purchaseQuantity, purchaseSet);
	}
	
	private long getLackQuantity(long promotionSet, long purchaseQuantity) {
		return promotionSet - purchaseQuantity;
	}
	
	private boolean canGetForFree(Product product, long purchaseQuantity, long promotionSet, long lackCount) {
		int get = product.getPromotion().getGet();
		
		if (lackCount == get && isLessThanProductQuantity(purchaseQuantity + lackCount, product.getQuantity())) {
			if (askForFreeProduct(product.getName(), lackCount)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isLessThanProductQuantity(long purchaseQuantity, long productQuantity) {
		return purchaseQuantity <= productQuantity;
	}
	
	private boolean askForFreeProduct(String productName, long lackCount) {
		System.out.println(NOW + productName + SUBJECT + lackCount + FREE_OF_CHARGE);
		
		while (true) {
			try {
				return inputView.askForFreeProduct(productName, lackCount);
			} catch (IllegalArgumentException e) {
				outputView.printErrorMessage(e.getMessage());
			}
		}
	}
	
	private boolean addProduct(Product product, long paymentQuantity, long presentQuantity) {
		orderRepository.addPayment(product, paymentQuantity);
		return orderRepository.addPresent(product, presentQuantity);
	}
	
	private void nonCoveredPromotion(Product product, long purchaseQuantity, long totalQuantity) {
		orderRepository.addPayment(product, purchaseQuantity);
	}
	
	private Clock getClock() {
		return Clock.fixed(
    	        LocalDate.of(2024, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant(),
    	        ZoneId.systemDefault()
    	    );
	}
}
