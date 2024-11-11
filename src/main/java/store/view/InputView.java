package store.view;

import java.util.List;

public interface InputView {
	List<String> readPurchaseItems();
	boolean askForNonCoveredPromotionQuantity(String productName, long nonCoveredPromotionQuantity);
	boolean askForFreeProduct(String productName, long lackCount);
	boolean isMembershipDiscount();
	boolean isContinueShopping();
}
