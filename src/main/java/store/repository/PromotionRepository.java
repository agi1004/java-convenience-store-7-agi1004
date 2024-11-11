package store.repository;

import java.util.List;
import java.util.Optional;

import store.domain.Promotion;

public interface PromotionRepository {
	List<Promotion> findAll();
	Optional<Promotion> findByName(String name);
}
