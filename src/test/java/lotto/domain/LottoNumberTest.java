package lotto.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LottoNumberTest {

    @DisplayName("1~45 범위 밖이면 예외를 던진다")
    @ParameterizedTest(name = "잘못된 값: {0}")
    @ValueSource(ints = {0, -1, 46, 100})
    void 범위_밖_예외(int invalid) {
        assertThatThrownBy(() -> new LottoNumber(invalid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("[ERROR]");
    }

    @DisplayName("경계값 1과 45는 허용된다")
    @ParameterizedTest
    @ValueSource(ints = {1, 45})
    void 경계값_허용(int valid) {
        assertThatCode(() -> new LottoNumber(valid))
                .doesNotThrowAnyException();
    }

    @DisplayName("값이 같으면 동치이며 해시도 같다")
    @Test
    void 값기반_동치성() {
        LottoNumber a = new LottoNumber(10);
        LottoNumber b = new LottoNumber(10);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @DisplayName("정렬 기준은 숫자 오름차순이다(선택)")
    @Test
    void 정렬_가능성() {
        LottoNumber a = new LottoNumber(12);
        LottoNumber b = new LottoNumber(1);
        LottoNumber c = new LottoNumber(45);

        assertThat(java.util.stream.Stream.of(a, b, c).sorted())
                .containsExactly(b, a, c);
    }
}