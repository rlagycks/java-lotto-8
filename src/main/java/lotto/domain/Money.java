package lotto.domain;

import java.math.BigDecimal;
import java.util.Objects;

public final class Money {
    private final BigDecimal amount; // scale=0, 원 단위

    private Money(BigDecimal amount) {
        // 금액은 원 단위 정수로 고정
        this.amount = amount.setScale(0);
    }

    public static Money of(long won) {
        return new Money(BigDecimal.valueOf(won));
    }

    public BigDecimal amount() {
        return amount; // 불변(BigDecimal) 반환
    }

    public Money plus(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money times(long n) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(n)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros());
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }
}