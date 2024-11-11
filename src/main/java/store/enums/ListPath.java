package store.enums;

public enum ListPath {
	PRODUCT_PATH("src/main/resources/products.md"),
	PROMOTION_PATH("src/main/resources/promotions.md");
	
	private final String value;
	
	ListPath(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
