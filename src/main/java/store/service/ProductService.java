package store.service;

import java.util.List;

import store.dto.ProductDto;

public interface ProductService {
	List<ProductDto> getAll();
	void purchase();
	void init();
}
