package lotto.view;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InputViewTest {
    private final InputView view = new InputView();

    @DisplayName("당첨 번호에 중복이 있으면 입력 단계에서 예외를 던진다")
    @Test
    void 당첨번호_중복_예외() {
        String line = "1,2,3,3,4,5";
        assertThatThrownBy(() -> view.parseWinningNumbers(line))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("[ERROR]")
                .hasMessageContaining("당첨 번호에 중복이 있습니다.");
    }
}