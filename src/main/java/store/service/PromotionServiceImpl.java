package store.service;

import java.util.List;

import store.domain.Promotion;
import store.repository.PromotionRepository;
import store.repository.PromotionRepositoryImpl;

public class PromotionServiceImpl implements PromotionService {
	private static final PromotionService INSTANCE;
	private final PromotionRepository promotionRepository;
	
	static {
		INSTANCE = new PromotionServiceImpl(PromotionRepositoryImpl.getInstance());
	}
	
	private PromotionServiceImpl(final PromotionRepository promotionRepository) {
		this.promotionRepository = promotionRepository;
	}
	
	public static PromotionService getInstance() {
		return INSTANCE;
	}
	
	@Override
	public List<Promotion> getAll() {
		return promotionRepository.findAll();
	}
}
