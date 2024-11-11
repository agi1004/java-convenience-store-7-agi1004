package store.dto;

import java.text.DecimalFormat;

import store.domain.Product;
import store.domain.Promotion;

public class ProductDto {
	private final String name;
	private final long price;
	private final long quantity;
	private final Promotion promotion;
	
	private ProductDto(final String name, final long price, 
					   final long quantity, final Promotion promotion) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.promotion = promotion;
	}
	
	public static ProductDto of(final Product product) {
		return new ProductDto(product.getName(), product.getPrice(), 
						      product.getQuantity(), product.getPromotion());
	}
	
	public static ProductDto of(final String name, final long price, 
							    final long quantity, final Promotion promotion) {
		return new ProductDto(name, price, quantity, promotion);
	}
	
	@Override
	public String toString() {
		DecimalFormat formatter = new DecimalFormat("###,###");
		return "- " + name + " " + formatter.format(price) + "원 " + parseQuantity() + parsePromotion();
	}

	public String getName() {
		return name;
	}

	public long getPrice() {
		return price;
	}

	public long getQuantity() {
		return quantity;
	}

	public Promotion getPromotion() {
		return promotion;
	}
	
	private String parseQuantity() {
		if (quantity == 0) {
			return "재고 없음 ";
		}
		
		return quantity + "개 ";
	}
	
	private String parsePromotion() {
		if (promotion == null) {
			return "";
		}
		
		return promotion.getName();
	}
}
