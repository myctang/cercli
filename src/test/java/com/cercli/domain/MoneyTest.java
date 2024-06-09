package com.cercli.domain;

import org.junit.jupiter.api.Test;

import static com.cercli.domain.Currency.USD;
import static org.assertj.core.api.Assertions.assertThat;

public class MoneyTest {
    @Test
    void returns_true_when_amount_is_positive() {
        // given
        var money = new Money(100, USD);

        // when
        var result = money.isPositive();

        // then
        assertThat(result).isTrue();
    }

    @Test
    void returns_false_when_amount_is_negative() {
        // given
        var money = new Money(-100, USD);

        // when
        var result = money.isPositive();

        // then
        assertThat(result).isFalse();
    }
}
