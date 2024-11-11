package store.dto;

import java.time.LocalDate;

import store.domain.Promotion;

public class PromotionDto {
	private final String name;
	private final int buy;
	private final int get;
	private final LocalDate startDate;
	private final LocalDate endDate;
	
	private PromotionDto(final String name, final int buy, final int get,
            		     final LocalDate startDate, final LocalDate endDate) {
		this.name = name;
		this.buy = buy;
		this.get = get;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public static PromotionDto of(final Promotion promotion) {
		return new PromotionDto(promotion.getName(), promotion.getBuy(), promotion.getGet(), 
								promotion.getStartDate(), promotion.getEndDate());
	}
	
	public static PromotionDto of(final String name, final int buy, final int get,
					              final LocalDate startDate, final LocalDate endDate) {
		return new PromotionDto(name, buy, get, startDate, endDate);
	}

	public String getName() {
		return name;
	}

	public int getBuy() {
		return buy;
	}

	public int getGet() {
		return get;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}
}
