package store.view;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import store.domain.Product;
import store.dto.AmountDto;
import store.dto.OrderDto;
import store.dto.ProductDto;

import static store.constant.PrintMessage.OUTPUT_PRODUCTS;
import static store.constant.PrintMessage.W_CONVENIENCE_STORE;
import static store.constant.PrintMessage.TAB_ONCE;
import static store.constant.PrintMessage.TAB_TWICE;
import static store.constant.PrintMessage.PAYMENT;
import static store.constant.PrintMessage.PRESENT;
import static store.constant.PrintMessage.LINE;
import static store.constant.PrintMessage.TOTAL_AMOUNT;
import static store.constant.PrintMessage.PRESENT_DISCOUNT_AMOUNT;
import static store.constant.PrintMessage.MEMBERSHIP_DISCOUNT_AMOUNT;
import static store.constant.PrintMessage.FINAL_AMOUNT;
import static store.constant.Regex.NUMBER_FORMAT;

public class OutputViewImpl implements OutputView {
	private static final OutputView INSTANCE;
	
	static {
		INSTANCE = new OutputViewImpl();
	}
	
	private OutputViewImpl() {}
	
	public static OutputView getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void printProducts(final List<ProductDto> productDtos) {
		System.out.println(OUTPUT_PRODUCTS);
		productDtos.stream().forEach(System.out::println);
	}
	
	@Override
	public void printErrorMessage(String message) {
		System.out.println(message);
	}

	@Override
	public void printReceipt(OrderDto orderDto, AmountDto amountDto) {
		System.out.println(W_CONVENIENCE_STORE);
		printPayments(orderDto.getPayments());
		printPresents(orderDto.getPresents());
		printPurchaseAmount(amountDto);
	}
	
	private void printPayments(Map<Product, Long> payments) {
		System.out.println(PAYMENT);
		for (Product product : payments.keySet()) {
			long purchaseQuantity = payments.get(product);
			printPayment(product.getName(), purchaseQuantity, product.getAmount(purchaseQuantity));
		}
	}
	
	private void printPayment(String name, long quantity, long purchaseAmount) {
		System.out.println(name + TAB_TWICE + quantity + TAB_ONCE + parse(purchaseAmount));
	}
	
	private void printPresents(Map<Product, Long> presents) {
		System.out.println(PRESENT);
		for (Product product : presents.keySet()) {
			printPresent(product.getName(), presents.get(product));
		}
	}
	
	private void printPresent(String name, long quantity) {
		System.out.println(name + TAB_TWICE + quantity);
	}
	
	private void printPurchaseAmount(AmountDto amountDto) {
		System.out.println(LINE);
		printTotal(amountDto.getTotalQuantity(), amountDto.getTotalAmount());
		printPresentDiscountAmount(amountDto.getPresentDiscountAmount());
		printMembershipDiscountAmount(amountDto.getMembershipDiscountAmount());
		printFinalAmount(amountDto.getFinalAmount());
	}
	
	private void printTotal(long totalQuantity, long totalAmount) {
		System.out.println(TOTAL_AMOUNT + totalQuantity + TAB_ONCE + parse(totalAmount));
	}
	
	private void printPresentDiscountAmount(long presentDiscountAmount) {
		System.out.println(PRESENT_DISCOUNT_AMOUNT + parse(presentDiscountAmount));
	}
	
	private void printMembershipDiscountAmount(long membershipDiscountAmount) {
		System.out.println(MEMBERSHIP_DISCOUNT_AMOUNT + parse(membershipDiscountAmount));
	}
	
	private void printFinalAmount(long finalAmount) {
		System.out.println(FINAL_AMOUNT + parse(finalAmount));
	}
	
	private String parse(long amount) {
		DecimalFormat formatter = new DecimalFormat(NUMBER_FORMAT);
		return formatter.format(amount);
	}
}
