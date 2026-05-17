package com.ey.vendingmachine.controller;

import com.ey.vendingmachine.model.request.ProductRequest;
import com.ey.vendingmachine.model.response.ProductListResponse;
import com.ey.vendingmachine.model.response.ProductStockResponse;
import com.ey.vendingmachine.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static java.util.Collections.emptyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

    }

    @Test
    public void givenProducts_whenGetProducts_thenReturns200WithList() throws Exception {
        //given
        List<ProductListResponse> products = List.of(
                ProductListResponse.builder().id(1).name("Cola").price(2.5).stock(10).build(),
                ProductListResponse.builder().id(2).name("Water").price(1.0).stock(5).build()
        );
        when(productService.listAll(0)).thenReturn(products);

        //when
        ResultActions result = mockMvc.perform(get("/products"));

        //then
        verify(productService).listAll(0);
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Cola"))
                .andExpect(jsonPath("$[0].price").value(2.5))
                .andExpect(jsonPath("$[0].stock").value(10))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Water"));
    }

    @Test
    public void givenNoProducts_whenGetProducts_thenReturns200WithEmptyList() throws Exception {
        //given
        when(productService.listAll(0)).thenReturn(emptyList());

        //when
        ResultActions result = mockMvc.perform(get("/products"));

        //then
        verify(productService).listAll(0);
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void givenPageParam_whenGetProducts_thenDelegatesToCorrectPage() throws Exception {
        //given
        when(productService.listAll(2)).thenReturn(emptyList());

        //when
        ResultActions result = mockMvc.perform(get("/products").param("page", "2"));

        //then
        verify(productService).listAll(2);
        result.andExpect(status().isOk());
    }

    @Test
    public void it_should_test_when_product_id_is_valid() throws Exception {
        //given
        ProductRequest productRequest = ProductRequest.builder().id(1).build();
        ArgumentCaptor<ProductRequest> argumentCaptor = ArgumentCaptor.forClass(ProductRequest.class);

        //when
        ResultActions result = mockMvc.perform(post("/products")
                .content(objectMapper.writeValueAsString(productRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        verify(productService).dispense(argumentCaptor.capture());
        result.andExpect(status().isOk());
    }

    @Test
    public void it_should_test_when_product_id_is_not_valid() throws Exception {
        //given
        ProductRequest productRequest = ProductRequest.builder().id(0).build();

        //when
        ResultActions result = mockMvc.perform(post("/products")
                .content(objectMapper.writeValueAsString(productRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        verifyNoInteractions(productService);
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void it_should_test_when_product_id_is_null() throws Exception {
        //given
        ProductRequest productRequest = ProductRequest.builder().build();

        //when
        ResultActions result = mockMvc.perform(post("/products")
                .content(objectMapper.writeValueAsString(productRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        verifyNoInteractions(productService);
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidProductId_whenGetStock_thenReturnsStockResponse() throws Exception {
        //given
        ProductStockResponse stockResponse = ProductStockResponse.builder()
                .productId(1)
                .productName("Cola")
                .stock(5)
                .build();
        when(productService.getStock(1)).thenReturn(stockResponse);

        //when
        ResultActions result = mockMvc.perform(get("/products/1/stock"));

        //then
        verify(productService).getStock(1);
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.productName").value("Cola"))
                .andExpect(jsonPath("$.stock").value(5));
    }
}