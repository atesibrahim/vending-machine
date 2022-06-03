package com.ey.vendingmachine.service.coin;

import com.ey.vendingmachine.data.CoinAmount;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class CoinServiceImpl implements CoinService{

    private final CoinAmount coinAmount;

    @Override
    public String sum(Double add) {
        log.info("Coming coin will be added your total amount:{}", add);
        double result = add / 100.0;
        if (Objects.nonNull(coinAmount.getBalance())) {
            result = result + coinAmount.getBalance();
        }
        coinAmount.setBalance(result);
        return "your current balance is $"+result;
    }
}
