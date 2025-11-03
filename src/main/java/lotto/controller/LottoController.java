package lotto.controller;

import java.util.List;
import lotto.domain.Money;
import lotto.domain.WinningNumbers;
import lotto.service.LottoService;
import lotto.service.ServiceResult;
import lotto.tickets.Tickets;
import lotto.view.InputView;
import lotto.view.OutputView;

public final class LottoController {
    private static final int PRICE_PER_TICKET = 1_000;

    private final InputView input;
    private final OutputView output;
    private final LottoService service;

    public LottoController(InputView input, OutputView output, LottoService service) {
        this.input = input;
        this.output = output;
        this.service = service;
    }

    public void run() {
        Money amount = readAmountWithRetry();
        Tickets tickets = new Tickets(service.issueTickets(amount));
        output.printTickets(tickets);

        List<Integer> wins = readWinningNumbersWithRetry();
        WinningNumbers winning = readBonusAndBuildWinningWithRetry(wins);

        ServiceResult result = service.judgeAll(tickets.asList(), winning, amount);
        output.printStatistics(result);
    }

    private Money readAmountWithRetry() {
        while (true) {
            try {
                String line = input.prompt("구입금액을 입력해 주세요.");
                long won = Long.parseLong(line.trim());
                validatePurchaseAmount(won);
                return Money.of(won);
            } catch (NumberFormatException e) {
                output.printError("[ERROR] 구입 금액은 1,000원 단위의 양수여야 합니다.");
            } catch (IllegalArgumentException | IllegalStateException e) {
                output.printError(e.getMessage());
            }
        }
    }

    private void validatePurchaseAmount(long won) {
        if (won <= 0 || won % PRICE_PER_TICKET != 0) {
            throw new IllegalArgumentException("[ERROR] 구입 금액은 1,000원 단위의 양수여야 합니다.");
        }
    }

    private List<Integer> readWinningNumbersWithRetry() {
        while (true) {
            try {
                String line = input.prompt("당첨 번호를 입력해 주세요.");
                return input.parseWinningNumbers(line);
            } catch (IllegalArgumentException | IllegalStateException e) {
                output.printError(e.getMessage());
            }
        }
    }

    private WinningNumbers readBonusAndBuildWinningWithRetry(List<Integer> wins) {
        while (true) {
            try {
                String line = input.prompt("보너스 번호를 입력해 주세요.");
                int bonus = input.parseBonus(line);
                preValidateBonus(wins, bonus);
                return new WinningNumbers(wins, bonus);
            } catch (IllegalArgumentException | IllegalStateException e) {
                output.printError(e.getMessage());
            }
        }
    }

    private void preValidateBonus(List<Integer> wins, int bonus) {
        if (bonus < 1 || bonus > 45) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 1부터 45 사이의 숫자여야 합니다.");
        }
        if (wins.contains(bonus)) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 당첨 번호와 중복될 수 없습니다.");
        }
    }
}