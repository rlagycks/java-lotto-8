package lotto.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lotto.domain.Lotto;

public final class WinningNumbers {
    private static final int SIZE = 6;
    private static final int MIN = 1;
    private static final int MAX = 45;

    private final Set<Integer> numbers;
    private final int bonus;

    public WinningNumbers(List<Integer> numbers, int bonus) {
        validate(numbers, bonus);
        this.numbers = Set.copyOf(numbers);
        this.bonus = bonus;
    }

    public int matchCount(Lotto lotto) {
        int count = 0;
        for (int n : lotto.getNumbers()) {
            if (numbers.contains(n)) count++;
        }
        return count;
    }

    public boolean bonusMatched(Lotto lotto) {
        for (int n : lotto.getNumbers()) {
            if (n == bonus) return true;
        }
        return false;
    }

    private void validate(List<Integer> numbers, int bonus) {
        if (numbers == null || numbers.size() != SIZE) {
            throw new IllegalArgumentException("[ERROR] 당첨 번호는 6개여야 합니다.");
        }
        if (hasOutOfRange(numbers) || bonus < MIN || bonus > MAX) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.");
        }
        if (new HashSet<>(numbers).size() != SIZE) {
            throw new IllegalArgumentException("[ERROR] 당첨 번호에 중복이 있습니다.");
        }
        if (numbers.contains(bonus)) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 당첨 번호와 중복될 수 없습니다.");
        }
    }

    private boolean hasOutOfRange(List<Integer> nums) {
        for (Integer n : nums) {
            if (n == null || n < MIN || n > MAX) return true;
        }
        return false;
    }
}