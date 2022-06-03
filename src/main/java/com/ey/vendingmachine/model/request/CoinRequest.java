package com.ey.vendingmachine.model.request;


import com.ey.vendingmachine.validation.OneOf;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoinRequest {

	@NotNull
	@Positive
	@OneOf({ 5.0, 10.0, 25.0 })
	private Double insertedCoin;
}
