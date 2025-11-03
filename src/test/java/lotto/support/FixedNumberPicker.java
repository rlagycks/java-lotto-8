package lotto.support;

import java.util.List;
import lotto.port.NumberPicker;

public final class FixedNumberPicker implements NumberPicker {
    private final List<Integer> numbers;

    public FixedNumberPicker(List<Integer> numbers) {
        this.numbers = List.copyOf(numbers);
    }

    @Override
    public List<Integer> pick() {
        return numbers;
    }
}