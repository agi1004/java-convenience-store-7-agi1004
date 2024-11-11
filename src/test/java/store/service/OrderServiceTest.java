package store.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import store.constant.ExceptionMessage;
import store.domain.Product;
import store.domain.Promotion;
import store.dto.OrderDto;
import store.dto.ProductDto;
import store.dto.PromotionDto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

class OrderServiceTest {
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = OrderServiceImpl.getInstance();
    }
    
    @AfterEach
    void tearDown() {
        orderService.reset();
    }

    @Test
    @DisplayName("존재하지 않는 상품으로 주문시 예외가 발생한다")
    void orderWithNonExistentProduct() {
        // given
        List<String> items = Arrays.asList("[없는상품-1]");

        // when & then
        assertThatThrownBy(() -> orderService.order(items))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.NON_EXISTENT_PRODUCT);
    }

    @Test
    @DisplayName("재고보다 많은 수량 주문시 예외가 발생한다")
    void orderExceedingQuantity() {
        // given
        List<String> items = Arrays.asList("[콜라-100]");

        // when & then
        assertThatThrownBy(() -> orderService.order(items))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.EXCEEDED_QUANTITY);
    }

    @Test
    @DisplayName("프로모션이 적용된 상품 주문시 증정품이 제공된다")
    void orderSuccessfully() {
    	PromotionDto promotionDto = PromotionDto.of("탄산2+1", 2, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
    	Promotion promotion = Promotion.of(promotionDto);
    	
        // given
        ProductDto productDto = ProductDto.of("콜라", 1000L, 5L, promotion);
        Product product = Product.of(productDto);
        List<String> items = Arrays.asList("[콜라-3]");

        // when
        OrderDto result = orderService.order(items);

        // then
        assertThat(result.getPayments()).containsKey(product);
        assertThat(result.getPayments().get(product)).isEqualTo(3L);
        assertThat(result.getPresents().get(product)).isEqualTo(1L);
    }

    @Test
    @DisplayName("프로모션이 적용되지 않은 상품 주문시 증정품이 제공되지 않는다")
    void orderWithPromotion() {
        // given
        ProductDto productDto = ProductDto.of("물", 500L, 10L, null);
        Product product = Product.of(productDto);
        List<String> items = Arrays.asList("[물-5]");

        // when
        OrderDto result = orderService.order(items);

        // then
        assertThat(result.getPayments()).containsKey(product);
        assertThat(result.getPayments().get(product)).isEqualTo(5L);
        assertThat(result.getPresents()).isEmpty();
    }

    @Test
    @DisplayName("멤버십 할인이 정상적으로 적용된다")
    void membershipDiscountApplied() {
        // given
        List<String> items = Arrays.asList("[정식도시락-8]");
        orderService.order(items);

        // when
        OrderDto result = orderService.membershipDiscount();

        // then
        assertTrue(result.isMembershipDiscount());
    }

    @Test
    @DisplayName("주문 초기화가 정상적으로 동작한다")
    void resetOrder() {
    	ProductDto productDto = ProductDto.of("물", 500L, 10L, null);
        Product product = Product.of(productDto);
    	
        List<String> items = Arrays.asList("[물-5]");
        OrderDto result = orderService.order(items);
        
        assertThat(result.getPayments().get(product)).isEqualTo(5L);
        assertThat(result.getPresents()).isEmpty();

        // when
        orderService.reset();
        
        productDto = ProductDto.of("에너지바", 2000L, 5L, null);
        product = Product.of(productDto);
        
        items = Arrays.asList("[에너지바-1]");
        result = orderService.order(items);

        assertThat(result.getPayments().get(product)).isEqualTo(1L);
        assertThat(result.getPresents()).isEmpty();
    }

    @Test
    @DisplayName("여러 상품을 동시에 주문할 수 있다")
    void orderMultipleProducts() {
        // given
        ProductDto product1Dto = ProductDto.of("정식도시락", 6400L, 8L, null);
        Product product1 = Product.of(product1Dto);
        
        ProductDto product2Dto = ProductDto.of("물", 500L, 10L, null);
        Product product2 = Product.of(product2Dto);
        
        List<String> items = Arrays.asList("[정식도시락-2]", "[물-3]");

        // when
        OrderDto result = orderService.order(items);

        // then
        assertThat(result.getPayments()).containsKeys(product1, product2);
        assertThat(result.getPayments().get(product1)).isEqualTo(2L);
        assertThat(result.getPayments().get(product2)).isEqualTo(3L);
    }
}



