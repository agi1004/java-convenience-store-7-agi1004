package store.dto;

import store.domain.Amount;

public class AmountDto {
	private final long totalQuantity;	// 총구매수량
	private final long totalAmount;	// 총구매액
	private final long presentDiscountAmount;	// 행사할인액
	private final long membershipDiscountAmount;	// 멤버십할인액
	private final long finalAmount;	// 내실돈
	
	private AmountDto(final long totalQuantity, final long totalAmount,
					  final long presentDiscountAmount, 
					  final long membershipDiscountAmount, final long finalAmount) {
		this.totalQuantity = totalQuantity;
		this.totalAmount = totalAmount;
		this.presentDiscountAmount = presentDiscountAmount;
		this.membershipDiscountAmount = membershipDiscountAmount;
		this.finalAmount = finalAmount;
	}
	
	public static AmountDto of(final Amount amount) {
		return new AmountDto(amount.getTotalQuantity(), amount.getTotalAmount(),
						     amount.getPresentDiscountAmount(), 
						     amount.getMembershipDiscountAmount(), amount.getFinalAmount());
	}
	
	public static AmountDto of(final long totalQuantity, final long totalAmount,
							   final long presentDiscountAmount, 
							   final long membershipDiscountAmount, final long finalAmount) {
		return new AmountDto(totalQuantity, totalAmount, presentDiscountAmount, 
							 membershipDiscountAmount, finalAmount);
	}

	public long getTotalQuantity() {
		return totalQuantity;
	}

	public long getTotalAmount() {
		return totalAmount;
	}

	public long getPresentDiscountAmount() {
		return presentDiscountAmount;
	}

	public long getMembershipDiscountAmount() {
		return membershipDiscountAmount;
	}

	public long getFinalAmount() {
		return finalAmount;
	}
}
