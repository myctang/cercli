package com.cercli.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class EmailTest {
    @Test
    void creates_an_email() {
        // given
        var email = "john@google.com";

        // when
        var result = Email.of(email);

        // then
        assertThat(result.asString()).isEqualTo(email);
    }

    @Test
    void throws__when_email_has_wrong_format() {
        // given
        var email = "test.test";

        // then
        assertThatThrownBy(() -> Email.of(email))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Invalid email format");
    }
}
