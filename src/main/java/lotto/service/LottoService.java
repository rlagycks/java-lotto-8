package lotto.service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lotto.domain.Lotto;
import lotto.domain.Money;
import lotto.domain.Rank;
import lotto.domain.Yield;
import lotto.domain.WinningNumbers;
import lotto.port.NumberPicker;

public final class LottoService {
    private static final int PRICE_PER_TICKET = 1_000;

    private final NumberPicker picker;

    public LottoService(NumberPicker picker) {
        this.picker = picker;
    }

    public List<Lotto> issueTickets(Money amount) {
        int count = toTicketCount(amount);
        List<Lotto> tickets = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            tickets.add(new Lotto(picker.pick()));
        }
        return List.copyOf(tickets);
    }

    public ServiceResult judgeAll(List<Lotto> tickets, WinningNumbers winning, Money spent) {
        Map<Rank, Integer> counts = new EnumMap<>(Rank.class);
        long totalPrize = 0L;

        for (Lotto ticket : tickets) {
            int match = winning.matchCount(ticket);
            boolean bonus = winning.bonusMatched(ticket);
            Rank rank = Rank.of(match, bonus);
            if (rank.isWinning()) {
                counts.merge(rank, 1, Integer::sum);
                totalPrize += rank.prize();
            }
        }
        return new ServiceResult(counts, Money.of(totalPrize), Yield.of(Money.of(totalPrize), spent));
    }

    private int toTicketCount(Money amount) {
        long won = amount.amount().longValueExact();
        if (won <= 0 || won % PRICE_PER_TICKET != 0) {
            throw new IllegalArgumentException("[ERROR] 구입 금액은 1,000원 단위의 양수여야 합니다.");
        }
        return (int) (won / PRICE_PER_TICKET);
    }
}