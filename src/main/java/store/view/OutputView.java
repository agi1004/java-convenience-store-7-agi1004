package store.view;

import java.util.List;

import store.dto.AmountDto;
import store.dto.OrderDto;
import store.dto.ProductDto;

public interface OutputView {
	void printProducts(List<ProductDto> productDtos);
	void printErrorMessage(String message);
	void printReceipt(OrderDto orderDto, AmountDto amountDto);
}
