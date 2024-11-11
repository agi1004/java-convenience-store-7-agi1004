package store.view;

import java.util.List;

import camp.nextstep.edu.missionutils.Console;
import store.choice.Choice;

import static store.constant.ExceptionMessage.INVALID_ITEMS_FORMAT;
import static store.constant.Regex.SEPARATOR;
import static store.constant.Regex.VALID_ITEMS_FORMAT;

public class InputViewImpl implements InputView {
	private static final InputView INSTANCE;
	
	static {
		INSTANCE = new InputViewImpl();
	}
	
	private InputViewImpl() {}
	
	public static InputView getInstance() {
		return INSTANCE;
	}
	
	@Override
	public List<String> readPurchaseItems() {
		String input = Console.readLine();
		validateFormat(input);
		return List.of(input.split(SEPARATOR));
	}
	
	@Override
	public boolean askForNonCoveredPromotionQuantity(String productName, long nonCoveredPromotionQuantity) {
		return response(Console.readLine());
	}
	
	@Override
	public boolean askForFreeProduct(String productName, long lackCount) {
		return response(Console.readLine());
	}
	
	@Override
	public boolean isMembershipDiscount() {
		return response(Console.readLine());
	}
	
	@Override
	public boolean isContinueShopping() {
		return response(Console.readLine());
	}

	private void validateFormat(String input) {
		if (!input.matches(VALID_ITEMS_FORMAT)) {
			throw new IllegalArgumentException(INVALID_ITEMS_FORMAT);
		}
	}
	
	private boolean response(String input) throws IllegalArgumentException {
		Choice.validateInput(input);
		return Choice.isYes(input);
	}
}
