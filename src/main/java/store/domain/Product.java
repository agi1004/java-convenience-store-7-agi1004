package store.domain;

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
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		
		Product other = (Product) obj;
		
		// name이 같은지 먼저 확인
	    if (!name.equals(other.name)) {
	        return false;
	    }
	    
	    // promotion이 null인 경우 처리
	    if (promotion == null && other.promotion != null) {
	        return false;
	    }
	    
	    if (promotion != null && other.promotion == null) {
	        return false;
	    }
	    
	    // promotion이 null이 아니면 promotion의 name이 같은지 확인
	    if (promotion != null && !promotion.getName().equals(other.getPromotion().getName())) {
	        return false;
	    }
		
		return true;
	}
}
