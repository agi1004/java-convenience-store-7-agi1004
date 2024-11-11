package store.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import store.domain.Product;
import store.domain.Promotion;
import store.dto.ProductDto;
import store.repository.PromotionRepository;
import store.repository.PromotionRepositoryImpl;

import static store.constant.Regex.SEPARATOR;
import static store.constant.Number.HEADER_LINE;
import static store.constant.Number.NULL;
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
		return br.lines().skip(HEADER_LINE)
				.map(this::generateProduct)
				.collect(Collectors.toList());
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
}
