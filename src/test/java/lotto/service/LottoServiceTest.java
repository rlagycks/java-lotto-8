package lotto.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import lotto.domain.Lotto;
import lotto.adapter.RandomsNumberPicker; // 사용 안 함: 컴파일만 확인
import lotto.domain.Money;
import lotto.domain.Rank;
import lotto.domain.Yield;
import lotto.port.NumberPicker;
import lotto.support.FixedNumberPicker;
import lotto.domain.WinningNumbers; // 이번 테스트에서 사용할 도메인
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LottoServiceTest {

    @DisplayName("구입 금액만큼 티켓을 발행한다 (1,000원/장)")
    @Test
    void issueTickets_count_by_amount() {
        NumberPicker picker = new FixedNumberPicker(List.of(1,2,3,4,5,6));
        LottoService service = new LottoService(picker);

        List<Lotto> tickets = service.issueTickets(Money.of(8_000));

        assertThat(tickets).hasSize(8);
        // 각 티켓은 오름차순 정렬된 동일 번호
        assertThat(tickets.get(0).getNumbers()).containsExactly(1,2,3,4,5,6);
    }

    @DisplayName("당첨 번호와 비교하여 등수별 개수와 수익률을 집계한다")
    @Test
    void judge_and_yield_statistics() {
        NumberPicker picker = new FixedNumberPicker(List.of(1,2,3,4,5,6));
        LottoService service = new LottoService(picker);

        // 8장 발행
        List<Lotto> tickets = service.issueTickets(Money.of(8_000));

        WinningNumbers winning = new WinningNumbers(List.of(1,2,3,4,5,6), 7);
        ServiceResult result = service.judgeAll(tickets, winning, Money.of(8_000));

        assertThat(result.countOf(Rank.FIRST)).isEqualTo(8);
        assertThat(result.totalPrize().toString()).isEqualTo("16000000000"); // 2,000,000,000 * 8
        assertThat(result.yield().asPercent()).isEqualTo("200,000,000.0%"); // (16,000,000,000 / 8,000)*100
    }
}