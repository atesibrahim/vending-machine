package com.ey.vendingmachine.controller;

import com.ey.vendingmachine.model.request.CoinRequest;
import com.ey.vendingmachine.service.coin.CoinService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coins")
@AllArgsConstructor
public class CoinController {

    private final CoinService coinService;

    @PostMapping
    public String sum(@RequestBody @Validated CoinRequest coinRequest) {
        return coinService.sum(coinRequest.getInsertedCoin());
    }
}