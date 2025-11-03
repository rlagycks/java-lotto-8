package lotto.adapter;

import camp.nextstep.edu.missionutils.Randoms;
import java.util.List;
import lotto.port.NumberPicker;

public final class RandomsNumberPicker implements NumberPicker {
    private static final int MIN = 1;
    private static final int MAX = 45;
    private static final int SIZE = 6;

    @Override
    public List<Integer> pick() {
        // Randoms 보장: [MIN, MAX] 범위에서 '중복 없이' SIZE개
        return Randoms.pickUniqueNumbersInRange(MIN, MAX, SIZE);
    }
}