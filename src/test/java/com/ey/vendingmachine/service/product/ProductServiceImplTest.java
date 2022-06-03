package com.ey.vendingmachine.service.product;

import com.ey.vendingmachine.data.CoinAmount;
import com.ey.vendingmachine.domain.Product;
import com.ey.vendingmachine.model.response.ProductDispenseResponse;
import com.ey.vendingmachine.model.request.ProductRequest;
import com.ey.vendingmachine.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CoinAmount coinAmount;

    @Test
    public void it_should_return_dispense_product_and_thank_you() {
        //Given
        Integer id = 1;
        Product product = Product.builder().id(1).name("name").price(5.0).stock(10).build();
        ProductRequest productRequest = ProductRequest.builder().id(1).build();

        //when
        when(coinAmount.getBalance()).thenReturn(10.0);
        when(productRepository.findById(id)).thenReturn(Optional.ofNullable(product));
        ProductDispenseResponse result = productService.dispense(productRequest);

        //Then
        verify(productRepository).findById(id);
        assertThat(result.getProductName()).isEqualTo("name");
        assertThat(result.getResponseMessage()).isEqualTo("THANK YOU");
        assertThat(result.getCurrentBalance()).isEqualTo(10.0);
    }

    @Test
    public void it_should_return_sold_out_when_out_of_stock() {
        //Given
        Integer id = 1;
        Product product = Product.builder().id(1).name("name").price(5.0).stock(0).build();
        ProductRequest productRequest = ProductRequest.builder().id(1).build();

        //when
        when(coinAmount.getBalance()).thenReturn(10.0);
        when(productRepository.findById(id)).thenReturn(Optional.ofNullable(product));
        ProductDispenseResponse result = productService.dispense(productRequest);

        //Then
        verify(productRepository).findById(id);
        assertThat(result.getResponseMessage()).isEqualTo("SOLD OUT");
        assertThat(result.getCurrentBalance()).isEqualTo(10.0);
    }

    @Test
    public void it_should_return_unsufficient_message_when_balance_not_enough() {
        //Given
        Integer id = 1;
        Product product = Product.builder().id(1).name("name").price(5.0).stock(3).build();
        ProductRequest productRequest = ProductRequest.builder().id(1).build();

        //when
        when(coinAmount.getBalance()).thenReturn(1.0);
        when(productRepository.findById(id)).thenReturn(Optional.ofNullable(product));
        ProductDispenseResponse result = productService.dispense(productRequest);

        //Then
        verify(productRepository).findById(id);
        assertThat(result.getResponseMessage()).isEqualTo("Unfortunately your balance is not sufficient.");
        assertThat(result.getCurrentBalance()).isEqualTo(1.0);
    }
}