package lotto.domain;

public enum Rank {
    FIRST(6, false, 2_000_000_000L, "6개 일치 (2,000,000,000원)"),
    SECOND(5, true,   30_000_000L, "5개 일치, 보너스 볼 일치 (30,000,000원)"),
    THIRD(5, false,    1_500_000L, "5개 일치 (1,500,000원)"),
    FOURTH(4, false,       50_000L, "4개 일치 (50,000원)"),
    FIFTH(3, false,         5_000L, "3개 일치 (5,000원)"),
    MISS(0, false,               0L, "꽝");

    private final int matchCount;
    private final boolean requiresBonus; // 의미상 SECOND만 true
    private final long prize;
    private final String label;

    Rank(int matchCount, boolean requiresBonus, long prize, String label) {
        this.matchCount = matchCount;
        this.requiresBonus = requiresBonus;
        this.prize = prize;
        this.label = label;
    }

    public static Rank of(int matchCount, boolean bonusMatched) {
        if (matchCount == 6) return FIRST;
        if (matchCount == 5 && bonusMatched) return SECOND;
        if (matchCount == 5) return THIRD;
        if (matchCount == 4) return FOURTH;
        if (matchCount == 3) return FIFTH;
        return MISS;
    }

    public boolean isWinning() {
        return this != MISS;
    }

    public long prize() {
        return prize;
    }

    public String label() {
        return label;
    }

    public int matchCount() {
        return matchCount;
    }

    public boolean requiresBonus() {
        return requiresBonus;
    }
}