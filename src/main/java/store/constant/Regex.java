package store.constant;

public final class Regex {
	private static final String VALID_ONE_ITEM_FORMAT = "\\[[가-힣]+-(\\d+)\\]";
	public static final String SEPARATOR = ",";
	public static final String ITEM_SEPARATOR = "-";
	public static final String VALID_ITEMS_FORMAT = "^" + VALID_ONE_ITEM_FORMAT + "(" + SEPARATOR + VALID_ONE_ITEM_FORMAT + ")*$";
	public static final String NUMBER_FORMAT = "###,###";
}
