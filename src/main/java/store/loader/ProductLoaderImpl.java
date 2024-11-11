package store.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import store.constant.Const;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.ProductDto;
import store.repository.PromotionRepository;
import store.repository.PromotionRepositoryImpl;

import static store.constant.Regex.SEPARATOR;
import static store.constant.Const.NULL;
import static store.enums.ProductIndex.NAME;
import static store.enums.ProductIndex.PRICE;
import static store.enums.ProductIndex.QUANTITY;
import static store.enums.ProductIndex.PROMOTION;
import static store.enums.ListPath.PRODUCT_PATH;

public class ProductLoaderImpl implements ProductLoader {
	private static final ProductLoader INSTANCE;
	private final PromotionRepository promotionRepository;
	
	static {
		INSTANCE = new ProductLoaderImpl(PromotionRepositoryImpl.getInstance());
	}
	
	private ProductLoaderImpl(final PromotionRepository promotionRepository) {
		this.promotionRepository = promotionRepository;
	}
	
	public static ProductLoader getInstance() {
		return INSTANCE;
	}
	
	@Override
	public List<Product> load() {
		try (BufferedReader br = new BufferedReader(new FileReader(PRODUCT_PATH.getValue()))) {
			return readProductList(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<>();
	}
	
	private List<Product> readProductList(final BufferedReader br) throws IOException {
		List<Product> products = new ArrayList<>();
		String line;
		br.readLine();
		
		while ((line = br.readLine()) != null) {
			addProduct(line, products);
		}
		
		return products;
	}
	
	private void addProduct(String line, List<Product> products) {
		Product product = generateProduct(line);
		products.add(product);
		checkProduct(products, product);
	}
	
	private Product generateProduct(final String line) {
		String[] data = line.split(SEPARATOR);
		
		String name = data[NAME.getValue()];
		int price = Integer.parseInt(data[PRICE.getValue()]);
		int quantity = Integer.parseInt(data[QUANTITY.getValue()]);
		Promotion promotion = parsePromotion(data[PROMOTION.getValue()]);
		
		return Product.of(ProductDto.of(name, price, quantity, promotion));
	}
	
	private Promotion parsePromotion(final String promotionName) {
		if (promotionName.equals(NULL)) {
			return null;
		}
		
		return promotionRepository.findByName(promotionName).orElse(null);
	}
	
	private void checkProduct(List<Product> products, Product product) {
		if (mustAddAnotherProduct(product)) {
			products.add(getAnotherProduct(product));
		}
	}
	
	private boolean mustAddAnotherProduct(Product product) {
		return product.getName().equals(Const.REQUIRED_PRODUCT1) || product.getName().equals(Const.REQUIRED_PRODUCT2);
	}
	
	private Product getAnotherProduct(Product product) {
		ProductDto productDto = ProductDto.of(product.getName(), product.getPrice(), 0, null);
		return Product.of(productDto);
	}
}
