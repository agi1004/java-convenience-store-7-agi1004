package store.domain;

import store.dto.AmountDto;

public class Amount {
	private long totalQuantity;	// 총구매수량
	private long totalAmount;	// 총구매액
	private long presentDiscountAmount;	// 행사할인액
	private long membershipDiscountAmount;	// 멤버십할인액
	private long finalAmount;	// 내실돈
	
	private Amount(final AmountDto amountDto) {
		this.totalQuantity = amountDto.getTotalQuantity();
		this.totalAmount = amountDto.getTotalAmount();
		this.presentDiscountAmount = amountDto.getPresentDiscountAmount();
		this.membershipDiscountAmount = amountDto.getMembershipDiscountAmount();
		this.finalAmount = amountDto.getFinalAmount();
	}
	
	public static Amount of(final AmountDto amountDto) {
		return new Amount(amountDto);
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
