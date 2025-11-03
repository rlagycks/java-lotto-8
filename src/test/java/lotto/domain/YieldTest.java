package lotto.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class YieldTest {

    @DisplayName("총상금 5,000 / 총구매 8,000 => 62.5%")
    @Test
    void 예시_625() {
        Money prize = Money.of(5_000);
        Money spent = Money.of(8_000);

        Yield y = Yield.of(prize, spent);
        assertThat(y.asPercent()).isEqualTo("62.5%");
    }

    @DisplayName("총상금 8,000 / 총구매 8,000 => 100.0%")
    @Test
    void 정비율_100() {
        Money prize = Money.of(8_000);
        Money spent = Money.of(8_000);

        Yield y = Yield.of(prize, spent);
        assertThat(y.asPercent()).isEqualTo("100.0%");
    }

    @DisplayName("반올림(HALF_UP) 검증: 2/3 * 100 => 66.7%")
    @Test
    void 반올림_HALF_UP() {
        Money prize = Money.of(2);
        Money spent = Money.of(3);

        Yield y = Yield.of(prize, spent);
        assertThat(y.asPercent()).isEqualTo("66.7%");
    }

    @DisplayName("천 단위 콤마 포함: 8 / 0.0008 비슷한 스케일이 아닌 정수로 1,000,000.0% 만들기")
    @Test
    void 천단위_콤마() {
        Money prize = Money.of(80_000); // 80,000
        Money spent = Money.of(8);      // 8 → (80,000/8)*100 = 1,000,000.0%
        Yield y = Yield.of(prize, spent);

        assertThat(y.asPercent()).isEqualTo("1,000,000.0%");
    }

    @DisplayName("구매 금액이 0이면 예외")
    @Test
    void 구매0_예외() {
        Money prize = Money.of(1);
        Money spent = Money.of(0);

        assertThatThrownBy(() -> Yield.of(prize, spent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("[ERROR]");
    }
}