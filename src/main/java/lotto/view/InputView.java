package lotto.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class InputView {

    public String prompt(String message) {
        System.out.println(message);
        return Console.readLine();
    }

    public List<Integer> parseWinningNumbers(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 당첨 번호는 쉼표(,)로 구분된 6개의 숫자여야 합니다.");
        }
        List<Integer> nums = Arrays.stream(line.split(","))
                .map(String::trim)
                .map(this::toInt)
                .collect(Collectors.toList());
        if (nums.size() != 6) {
            throw new IllegalArgumentException("[ERROR] 당첨 번호는 쉼표(,)로 구분된 6개의 숫자여야 합니다.");
        }
        long distinct = nums.stream().distinct().count();
        if (distinct != 6) {
            throw new IllegalArgumentException("[ERROR] 당첨 번호에 중복이 있습니다.");
        }
        return nums;
    }

    public int parseBonus(String line) {
        return toInt(line.trim());
    }

    private int toInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 숫자만 입력할 수 있습니다.");
        }
    }
}