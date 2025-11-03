package lotto.service;

import java.util.EnumMap;
import java.util.Map;
import lotto.domain.Money;
import lotto.domain.Rank;
import lotto.domain.Yield;

public final class ServiceResult {
    private final Map<Rank, Integer> counts; // 불변 맵으로 보관
    private final Money totalPrize;
    private final Yield yield;

    public ServiceResult(Map<Rank, Integer> counts, Money totalPrize, Yield yield) {
        this.counts = counts == null ? new EnumMap<>(Rank.class) : new EnumMap<>(counts);
        this.totalPrize = totalPrize;
        this.yield = yield;
    }

    public int countOf(Rank rank) {
        return counts.getOrDefault(rank, 0);
    }

    public Money totalPrize() {
        return totalPrize;
    }

    public Yield yield() {
        return yield;
    }
}