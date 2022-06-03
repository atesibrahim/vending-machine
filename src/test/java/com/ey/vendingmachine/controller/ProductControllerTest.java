package com.ey.vendingmachine.controller;

import com.ey.vendingmachine.model.request.CoinRequest;
import com.ey.vendingmachine.model.request.ProductRequest;
import com.ey.vendingmachine.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}