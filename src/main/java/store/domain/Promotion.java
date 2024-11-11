package store.domain;

import java.time.LocalDate;

import store.dto.PromotionDto;

public class Promotion {
	private String name;	// pk
	private int buy;
	private int get;
	private LocalDate startDate;
	private LocalDate endDate;
	
	private Promotion(final PromotionDto promotionDto) {
		this.name = promotionDto.getName();
		this.buy = promotionDto.getBuy();
		this.get = promotionDto.getGet();
		this.startDate = promotionDto.getStartDate();
		this.endDate = promotionDto.getEndDate();
	}
	
	public static Promotion of(final PromotionDto promotionDto) {
		return new Promotion(promotionDto);
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
