package com.ey.vendingmachine.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class OneOfValidator implements ConstraintValidator<OneOf, Double> {

    private static final List<Double> VALID_COIN_TYPES = List.of(5.0,10.0,25.0);

    @Override
    public boolean isValid(Double coinField,
                           ConstraintValidatorContext cxt) {
        return coinField != null && VALID_COIN_TYPES.contains(coinField);
    }
}
