package store.repository;

import java.util.List;
import java.util.Optional;

import store.domain.Product;

public interface ProductRepository {
	List<Product> findAll();
	Optional<Product> findByName(String name);
	void save(Product product);
	List<Product> findListInStockByName(String name);
}
