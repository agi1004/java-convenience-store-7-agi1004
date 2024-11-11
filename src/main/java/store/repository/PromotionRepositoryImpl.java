package store.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import store.domain.Promotion;
import store.loader.PromotionLoader;
import store.loader.PromotionLoaderImpl;

public class PromotionRepositoryImpl implements PromotionRepository {
	private static final PromotionRepository INSTANCE;
	private final List<Promotion> promotions = new ArrayList<>();;
	
	static {
		INSTANCE = new PromotionRepositoryImpl(PromotionLoaderImpl.getInstance());
	}
	
	private PromotionRepositoryImpl(final PromotionLoader promotionLoader) {
		this.promotions.clear();
		this.promotions.addAll(promotionLoader.load());
	}
	
	public static PromotionRepository getInstance() {
		return INSTANCE;
	}
	
	@Override
	public List<Promotion> findAll() {
		return new ArrayList<>(promotions);
	}

	@Override
	public Optional<Promotion> findByName(String name) {
		return promotions.stream()
				.filter(promotion -> name.equals(promotion.getName()))
				.findFirst();
	}

	@Override
	public void saveAll(List<Promotion> promotions) {
		this.promotions.clear();
		this.promotions.addAll(promotions);
	}
}
