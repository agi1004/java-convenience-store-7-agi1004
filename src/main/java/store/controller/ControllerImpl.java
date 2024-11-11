package store.controller;

import java.util.List;

import store.dto.AmountDto;
import store.dto.OrderDto;
import store.service.AmountService;
import store.service.AmountServiceImpl;
import store.service.ProductService;
import store.service.ProductServiceImpl;
import store.service.OrderService;
import store.service.OrderServiceImpl;
import store.view.InputView;
import store.view.InputViewImpl;
import store.view.OutputView;
import store.view.OutputViewImpl;

import static store.constant.PrintMessage.INPUT_PURCHASE_ITEMS;
import static store.constant.PrintMessage.INPUT_MEMBERSHIP_DISCOUNT;
import static store.constant.PrintMessage.INPUT_CONTINUE_SHOPPING;

public class ControllerImpl implements Controller {
	private static final Controller INSTANCE;
	private final ProductService productService;
	private final OrderService orderService;
	private final AmountService amountService;
	private final InputView inputView;
	private final OutputView outputView;
	
	static {
		INSTANCE = new ControllerImpl(ProductServiceImpl.getInstance(), 
									  OrderServiceImpl.getInstance(),
									  AmountServiceImpl.getInstance(),
								  	  InputViewImpl.getInstance(),
								  	  OutputViewImpl.getInstance());
	}
	
	private ControllerImpl(final ProductService productService, 
						   final OrderService orderService,
						   final AmountService amountService,
					   	   final InputView inputView, 
					   	   final OutputView outputView) {
		this.productService = productService;
		this.orderService = orderService;
		this.amountService = amountService;
		this.inputView = inputView;
		this.outputView = outputView;
	}
	
	public static Controller getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void run() {
		do {
			showProducts();
			OrderDto orderDto = orderProducts();
			showReceipt(purchase(orderDto));
		} while (isContinueShopping());
	}
	
	private void showProducts() {
		outputView.printProducts(productService.getAll());
	}
	
	private OrderDto orderProducts() {
		System.out.println(INPUT_PURCHASE_ITEMS);
		
		while (true) {
			try {
				List<String> items = inputView.readPurchaseItems();
				return orderService.order(items);
			} catch (IllegalArgumentException e) {
				outputView.printErrorMessage(e.getMessage());
			}
		}
	}
	
	private OrderDto purchase(OrderDto orderDto) {
		if (isMembershipDiscount()) {
			orderDto = orderService.membershipDiscount();
		}
		
		productService.purchase();
		
		return orderDto;
	}
	
	private boolean isMembershipDiscount() {
		System.out.println(INPUT_MEMBERSHIP_DISCOUNT);
		
		while (true) {
			try {
				return inputView.isMembershipDiscount();
			} catch (IllegalArgumentException e) {
				outputView.printErrorMessage(e.getMessage());
			}
		}
	}
	
	private void showReceipt(OrderDto orderDto) {
		AmountDto amountDto = amountService.calculate(orderDto);
		outputView.printReceipt(orderDto, amountDto);
		orderService.reset();
	}
	
	private boolean isContinueShopping() {
		System.out.println(INPUT_CONTINUE_SHOPPING);
		while (true) {
			try {
				return inputView.isContinueShopping();
			} catch (IllegalArgumentException e) {
				outputView.printErrorMessage(e.getMessage());
			}
		}
	}
}
