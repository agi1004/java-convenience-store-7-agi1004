package store.enums;

public enum PromotionIndex {
	NAME(0),
	BUY(1),
	GET(2),
	START_DATE(3),
	END_DATE(4);
	
	private final int value;
	
	PromotionIndex(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
