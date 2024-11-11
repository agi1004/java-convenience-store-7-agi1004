package store.enums;

public enum PurchaseItemIndex {
	NAME(0),
	QUANTITY(1);
	
	private final int value;
	
	PurchaseItemIndex(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
