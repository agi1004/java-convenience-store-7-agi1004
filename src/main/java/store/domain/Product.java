package store.domain;

import java.util.Objects;

import store.dto.ProductDto;

public class Product {
	private String name;
	private long price;
	private long quantity;
	private Promotion promotion;	// 매핑
	
	private Product(final ProductDto productDto) {
		this.name = productDto.getName();
		this.price = productDto.getPrice();
		this.quantity = productDto.getQuantity();
		this.promotion = productDto.getPromotion();
	}
	
	public static Product of(final ProductDto productDto) {
		return new Product(productDto);
	}
	
	public void reduceQuantity(long quantity) {
	    this.quantity -= quantity;
	}
	
	public long getAmount(long purchaseQuantity) {
		return price * purchaseQuantity;
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

	@Override
	public int hashCode() {
		return Objects.hash(name, promotion);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		Product other = (Product) obj;
		
		return Objects.equals(name, other.name) && Objects.equals(promotion, other.promotion);
	}
}
