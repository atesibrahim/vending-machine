package com.ey.vendingmachine.service.product;

import com.ey.vendingmachine.data.CoinAmount;
import com.ey.vendingmachine.domain.Product;
import com.ey.vendingmachine.model.response.ProductListResponse;
import com.ey.vendingmachine.model.response.ProductStockResponse;
import com.ey.vendingmachine.model.request.ProductRequest;
import com.ey.vendingmachine.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Collections.emptyList;

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
        var result = productService.dispense(productRequest);

        //Then
        verify(productRepository).findById(id);
        assertThat(result.getProductName()).isEqualTo("name");
        assertThat(result.getResponseMessage()).isEqualTo("THANK YOU");
        assertThat(result.getCurrentBalance()).isEqualTo(10.0);
    }

    @Test
    public void givenSoldOutProduct_whenDispense_thenThrowsIllegalStateException() {
        //Given
        Integer id = 1;
        Product product = Product.builder().id(1).name("name").price(5.0).stock(0).build();
        ProductRequest productRequest = ProductRequest.builder().id(1).build();
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        //when/then
        assertThatThrownBy(() -> productService.dispense(productRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("sold out");
    }

    @Test
    public void givenValidProductId_whenGetStock_thenReturnsStock() {
        //Given
        Integer id = 1;
        Product product = Product.builder().id(1).name("Cola").price(2.5).stock(7).build();
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        //when
        ProductStockResponse result = productService.getStock(id);

        //Then
        verify(productRepository).findById(id);
        assertThat(result.getProductId()).isEqualTo(1);
        assertThat(result.getProductName()).isEqualTo("Cola");
        assertThat(result.getStock()).isEqualTo(7);
    }

    @Test
    public void givenUnknownProductId_whenGetStock_thenThrowsIllegalArgumentException() {
        //Given
        Integer id = 99;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        //when/then
        assertThatThrownBy(() -> productService.getStock(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("99");
    }

    @Test
    public void givenInsufficientBalance_whenDispense_thenThrowsIllegalStateException() {
        //Given
        Integer id = 1;
        Product product = Product.builder().id(1).name("name").price(5.0).stock(3).build();
        ProductRequest productRequest = ProductRequest.builder().id(1).build();
        when(coinAmount.getBalance()).thenReturn(1.0);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        //when/then
        assertThatThrownBy(() -> productService.dispense(productRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("balance");
    }

    @Test
    public void givenProducts_whenListAll_thenReturnsPagedProducts() {
        //Given
        List<Product> products = List.of(
                Product.builder().id(1).name("Cola").price(2.5).stock(10).build(),
                Product.builder().id(2).name("Water").price(1.0).stock(5).build()
        );
        PageRequest pageable = PageRequest.of(0, 15);
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(products));

        //when
        List<ProductListResponse> result = productService.listAll(0);

        //Then
        verify(productRepository).findAll(pageable);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("Cola");
        assertThat(result.get(0).getPrice()).isEqualTo(2.5);
        assertThat(result.get(0).getStock()).isEqualTo(10);
        assertThat(result.get(1).getId()).isEqualTo(2);
        assertThat(result.get(1).getName()).isEqualTo("Water");
    }

    @Test
    public void givenNoProducts_whenListAll_thenReturnsEmptyList() {
        //Given
        PageRequest pageable = PageRequest.of(0, 15);
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(emptyList()));

        //when
        List<ProductListResponse> result = productService.listAll(0);

        //Then
        verify(productRepository).findAll(pageable);
        assertThat(result).isEmpty();
    }

    @Test
    public void givenMoreThan15Products_whenListAllPage0_thenReturnsOnly15() {
        //Given
        List<Product> fifteenProducts = List.of(
                Product.builder().id(1).name("P1").price(1.0).stock(1).build(),
                Product.builder().id(2).name("P2").price(1.0).stock(1).build(),
                Product.builder().id(3).name("P3").price(1.0).stock(1).build(),
                Product.builder().id(4).name("P4").price(1.0).stock(1).build(),
                Product.builder().id(5).name("P5").price(1.0).stock(1).build(),
                Product.builder().id(6).name("P6").price(1.0).stock(1).build(),
                Product.builder().id(7).name("P7").price(1.0).stock(1).build(),
                Product.builder().id(8).name("P8").price(1.0).stock(1).build(),
                Product.builder().id(9).name("P9").price(1.0).stock(1).build(),
                Product.builder().id(10).name("P10").price(1.0).stock(1).build(),
                Product.builder().id(11).name("P11").price(1.0).stock(1).build(),
                Product.builder().id(12).name("P12").price(1.0).stock(1).build(),
                Product.builder().id(13).name("P13").price(1.0).stock(1).build(),
                Product.builder().id(14).name("P14").price(1.0).stock(1).build(),
                Product.builder().id(15).name("P15").price(1.0).stock(1).build()
        );
        PageRequest pageable = PageRequest.of(0, 15);
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(fifteenProducts));

        //when
        List<ProductListResponse> result = productService.listAll(0);

        //Then
        assertThat(result).hasSize(15);
    }

    @Test
    public void givenUnknownProductId_whenDispense_thenThrowsIllegalArgumentException() {
        //Given
        Integer id = 99;
        ProductRequest productRequest = ProductRequest.builder().id(99).build();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        //when/then
        assertThatThrownBy(() -> productService.dispense(productRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("99");
    }
}