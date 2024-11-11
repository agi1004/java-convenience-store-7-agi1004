package store.service;

import java.util.Map;

import store.domain.Product;
import store.dto.AmountDto;
import store.dto.OrderDto;

import static store.constant.Const.ZERO;
import static store.constant.Const.MAX_MEMBERSHIP_LIMIT;
import static store.constant.Const.MEMBERSHIP_DISCOUNT_PERCENT;
import static store.constant.Const.UNIT;

public class AmountServiceImpl implements AmountService {
	private static final AmountService INSTANCE;
	
	static {
		INSTANCE = new AmountServiceImpl();
	}
	
	private AmountServiceImpl() {}
	
	public static AmountService getInstance() {
		return INSTANCE;
	}
	
	@Override
	public AmountDto calculate(OrderDto orderDto) {
		Map<Product, Long> payments = orderDto.getPayments();
		Map<Product, Long> presents = orderDto.getPresents();
		boolean membershipDiscount = orderDto.isMembershipDiscount();
		return getAmountDto(payments, presents, membershipDiscount);
	}
	
	private AmountDto getAmountDto(Map<Product, Long> payments, Map<Product, Long> presents, 
			   					   boolean membershipDiscount) {
		long totalQuantity = getTotalQuantity(payments);	// 총구매수량
		long totalAmount = getAmount(payments);				// 총구매액
		long presentDiscountAmount = getAmount(presents);	// 행사할인
		long coveredPromotionAmount = totalAmount - presentDiscountAmount;	// 총구매액 - 행사할인
		long membershipDiscountAmount = getMembershipDiscountAmount(membershipDiscount, coveredPromotionAmount); // 멤버십할인
		long finalAmount = coveredPromotionAmount - membershipDiscountAmount;	// 내실돈
		
		return AmountDto.of(totalQuantity, totalAmount, presentDiscountAmount,
			    membershipDiscountAmount, finalAmount);
	}
	
	private long getTotalQuantity(Map<Product, Long> payments) {
		return payments.values().stream()
				.mapToLong(i -> i)
				.sum();
	}
	
	private long getAmount(Map<Product, Long> map) {
		return map.keySet().stream()
				.mapToLong(product -> product.getAmount(map.get(product)))
				.sum();
	}
	
	private long getMembershipDiscountAmount(boolean membershipDiscount, long coveredPromotionAmount) {
		if (!membershipDiscount) {
			return ZERO;
		}
		
		return Math.min(MAX_MEMBERSHIP_LIMIT, (long)((coveredPromotionAmount * MEMBERSHIP_DISCOUNT_PERCENT) / UNIT) * UNIT);
	}
}
