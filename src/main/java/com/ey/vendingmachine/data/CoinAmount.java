package com.ey.vendingmachine.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@SessionScope
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoinAmount {

    private Double balance;
}
