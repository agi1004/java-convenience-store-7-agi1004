package store.enums;

public enum ProductIndex {
	NAME(0),
	PRICE(1),
	QUANTITY(2),
	PROMOTION(3);
	
	private final int value;
	
	ProductIndex(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
