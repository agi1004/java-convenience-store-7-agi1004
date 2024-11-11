package store.service;

import store.dto.AmountDto;
import store.dto.OrderDto;

public interface AmountService {
	AmountDto calculate(OrderDto orderDto);
}
