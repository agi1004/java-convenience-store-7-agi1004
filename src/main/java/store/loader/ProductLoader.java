package store.loader;

import java.util.List;

import store.domain.Product;

public interface ProductLoader {
	List<Product> load();
}
