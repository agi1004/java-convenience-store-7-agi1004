package store.enums;

import static store.constant.ExceptionMessage.INVALID_INPUT;

public enum Choice {
	YES("Y"),
	NO("N");
	
	private final String value;
	
	Choice(String value) {
		this.value = value;
	}
	
	public static boolean isYes(String input) {
		return YES.value.equals(input);
	}
	
	public static void validateInput(String input) {
		if (!YES.value.equals(input) && !NO.value.equals(input)) {
			throw new IllegalArgumentException(INVALID_INPUT);
		}
	}
}
