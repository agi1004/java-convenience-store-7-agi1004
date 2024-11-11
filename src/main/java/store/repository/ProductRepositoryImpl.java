package store.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import store.domain.Product;
import store.loader.ProductLoader;
import store.loader.ProductLoaderImpl;

public class ProductRepositoryImpl implements ProductRepository {
	private static final ProductRepository INSTANCE;
	private final List<Product> products = new ArrayList<>();
	
	static {
		INSTANCE = new ProductRepositoryImpl(ProductLoaderImpl.getInstance());
	}
	
	private ProductRepositoryImpl(final ProductLoader productLoader) {
		this.products.clear();
		this.products.addAll(productLoader.load());
		//this.products = productLoader.load();
	}
	
	public static ProductRepository getInstance() {
		return INSTANCE;
	}
	
	@Override
	public List<Product> findAll() {
		return new ArrayList<>(products);
	}
	
	@Override
	public Optional<Product> findByName(String name) {
		return products.stream()
				.filter(product -> name.equals(product.getName()))
				.findFirst();
	}

	@Override
	public void save(Product product) {
		products.set(findIndex(product), product);
		return;
	}
	
	@Override
	public List<Product> findListInStockByName(String name) {
		return findAll().stream()
				.filter(product -> name.equals(product.getName()) && product.getQuantity() != 0)
				.collect(Collectors.toList());
	}
	
	@Override
	public void saveAll(List<Product> products) {
		this.products.clear();
		this.products.addAll(products);
	}
	
	private int findIndex(Product product) {
		return IntStream.range(0, products.size())
				.filter(i -> products.get(i).equals(product))
				.findFirst()
				.orElse(-1);
	}
}


