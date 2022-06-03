package com.ey.vendingmachine.controller;

import com.ey.vendingmachine.model.request.CoinRequest;
import com.ey.vendingmachine.service.coin.CoinService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class CoinControllerTest {

    @InjectMocks
    private CoinController coinController;

    @Mock
    private CoinService coinService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(coinController).build();

    }

    @Test
    public void it_should_test_when_coin_is_valid() throws Exception {
        //given
        CoinRequest coinRequest = CoinRequest.builder().insertedCoin(25.0).build();

        //when
        ResultActions result = mockMvc.perform(post("/coins")
                .content(objectMapper.writeValueAsString(coinRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        verify(coinService).sum(coinRequest.getInsertedCoin());
        result.andExpect(status().isOk());
    }

    @Test
    public void it_should_test_when_coin_is_not_valid() throws Exception {
        //given
        CoinRequest coinRequest = CoinRequest.builder().insertedCoin(2.0).build();

        //when
        ResultActions result = mockMvc.perform(post("/coins")
                .content(objectMapper.writeValueAsString(coinRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        verifyNoInteractions(coinService);
        result.andExpect(status().isBadRequest());
    }
}