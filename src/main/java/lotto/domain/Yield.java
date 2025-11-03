package lotto.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

public final class Yield {
    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#,##0.0'%'");

    private final BigDecimal percent; // e.g., 62.5 (not 0.625)

    private Yield(BigDecimal percent) {
        // 내부 표현도 한 자리 소수로 고정
        this.percent = percent.setScale(1, RoundingMode.HALF_UP);
    }

    public static Yield of(Money totalPrize, Money totalSpent) {
        Objects.requireNonNull(totalPrize, "totalPrize");
        Objects.requireNonNull(totalSpent, "totalSpent");

        if (totalSpent.amount().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("[ERROR] 구매 금액이 0원일 수 없습니다.");
        }
        // (prize / spent) * 100 → 둘째 자리에서 반올림 → 한 자리 표시
        BigDecimal ratio = totalPrize.amount()
                .divide(totalSpent.amount(), 6, RoundingMode.HALF_UP) // 충분한 스케일로 나눈 뒤
                .multiply(HUNDRED);
        return new Yield(ratio);
    }

    public String asPercent() {
        return PERCENT_FORMAT.format(percent);
    }

    public BigDecimal value() {
        return percent;
    }
}