package lotto.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LottoValidationTest {

    @DisplayName("숫자가 6개 미만이면 예외")
    @Test
    void 숫자_6개_미만이면_예외() {
        assertThatThrownBy(() -> new Lotto(List.of(1, 2, 3, 4, 5)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("[ERROR]");
    }

    @DisplayName("범위(1~45) 밖의 숫자가 포함되면 예외")
    @Test
    void 범위_위반_예외() {
        assertThatThrownBy(() -> new Lotto(List.of(0, 2, 3, 4, 5, 46)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("[ERROR]");
    }

    @DisplayName("생성 시 번호는 오름차순으로 정렬되어 불변으로 보관된다")
    @Test
    void 생성하면_오름차순_정렬_불변() {
        Lotto lotto = new Lotto(List.of(6, 5, 4, 3, 2, 1));
        assertThat(lotto.getNumbers()).containsExactly(1, 2, 3, 4, 5, 6);
        assertThatThrownBy(() -> lotto.getNumbers().add(99))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}