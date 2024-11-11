package store.constant;

public final class ExceptionMessage {
	private static final String ERROR = "\n[ERROR] ";
	public static final String INVALID_ITEMS_FORMAT = ERROR + "올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
	public static final String NON_EXISTENT_PRODUCT = ERROR + "존재하지 않는 상품입니다. 다시 입력해 주세요.";
	public static final String EXCEEDED_QUANTITY = ERROR + "재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";
	public static final String OUT_OF_STOCK = ERROR + "재고가 모두 소진되었습니다. 다시 입력해 주세요.";
	public static final String INVALID_INPUT = ERROR + "잘못된 입력입니다. 다시 입력해 주세요.";
}
