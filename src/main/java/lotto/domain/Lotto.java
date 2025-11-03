package lotto.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Lotto {
    private static final int REQUIRED_SIZE = 6;
    private static final int MIN = 1;
    private static final int MAX = 45;

    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = toUnmodifiableSorted(numbers);
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    private void validate(List<Integer> numbers) {
        if (numbers == null || numbers.size() != REQUIRED_SIZE) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 6개여야 합니다.");
        }
        if (hasOutOfRange(numbers)) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.");
        }
        if (hasDuplicate(numbers)) {
            throw new IllegalArgumentException("[ERROR] 로또 번호에 중복이 있습니다.");
        }
    }

    private boolean hasOutOfRange(List<Integer> nums) {
        for (Integer n : nums) {
            if (n == null || n < MIN || n > MAX) return true;
        }
        return false;
    }

    private boolean hasDuplicate(List<Integer> nums) {
        return new HashSet<>(nums).size() != nums.size();
    }

    private List<Integer> toUnmodifiableSorted(List<Integer> src) {
        List<Integer> copy = new ArrayList<>(src);
        Collections.sort(copy);
        return Collections.unmodifiableList(copy);
    }
}