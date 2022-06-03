package com.ey.vendingmachine.service.coin;

import com.ey.vendingmachine.data.CoinAmount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CoinServiceImplTest {

    @InjectMocks
    private CoinServiceImpl coinService;

    @Mock
    private CoinAmount coinAmount;

    @Test
    public void it_should_sum_coin_when_first_value_is_null() {
        //Given
        Double insertCoin = 5.0;
        //when
        when(coinAmount.getBalance()).thenReturn(null);
        String result = coinService.sum(insertCoin);

        //Then
        assertThat(result).isEqualTo("your current balance is $0.05");
    }

    @Test
    public void it_should_sum_coin() {
        //Given
        Double insertCoin = 5.0;
        //when
        when(coinAmount.getBalance()).thenReturn(10.0);
        String result = coinService.sum(insertCoin);

        //Then
        assertThat(result).isEqualTo("your current balance is $10.05");
    }
}