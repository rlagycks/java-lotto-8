package lotto.controller;

import java.util.ArrayList;
import java.util.List;
import lotto.domain.Lotto;
import lotto.domain.Money;
import lotto.domain.WinningNumbers;
import lotto.service.LottoService;
import lotto.service.ServiceResult;
import lotto.tickets.Tickets;
import lotto.view.InputView;
import lotto.view.OutputView;

public final class LottoController {
    private final InputView input;
    private final OutputView output;
    private final LottoService service;

    public LottoController(InputView input, OutputView output, LottoService service) {
        this.input = input;
        this.output = output;
        this.service = service;
    }

    public void run() {
        Tickets tickets = readAmountAndIssueWithRetry();   // 금액 단계
        output.printTickets(tickets);                      // 구매 수량/티켓 출력

        List<Integer> wins = readWinningNumbersWithRetry();         // 당첨 번호 단계
        WinningNumbers winning = readBonusAndBuildWinningWithRetry(wins); // 보너스 단계

        ServiceResult result = service.judgeAll(tickets.asList(), winning, Money.of(tickets.size() * 1_000L));
        output.printStatistics(result);
    }

    private Tickets readAmountAndIssueWithRetry() {
        while (true) {
            try {
                String line = input.prompt("구입금액을 입력해 주세요.");
                long won = Long.parseLong(line.trim());
                Money amount = Money.of(won);
                List<Lotto> issued = service.issueTickets(amount); // 1,000원 단위 검증 포함
                return new Tickets(issued);
            } catch (IllegalArgumentException e) {
                output.printError(e.getMessage());
            } catch (Exception e) {
                output.printError("[ERROR] 구입 금액은 1,000원 단위의 양수여야 합니다.");
            }
        }
    }

    private List<Integer> readWinningNumbersWithRetry() {
        while (true) {
            try {
                System.out.println();
                String line = input.prompt("당첨 번호를 입력해 주세요.");
                List<Integer> wins = input.parseWinningNumbers(line);
                // 임시 보너스를 골라 유효성만 검증(보너스는 나중 단계에서 입력)
                int tmpBonus = pickTempBonus(wins);
                new WinningNumbers(wins, tmpBonus); // 범위/중복 검증 실패 시 예외
                return wins;
            } catch (IllegalArgumentException e) {
                output.printError(e.getMessage());
            }
        }
    }

    private WinningNumbers readBonusAndBuildWinningWithRetry(List<Integer> wins) {
        while (true) {
            try {
                System.out.println();
                String line = input.prompt("보너스 번호를 입력해 주세요.");
                int bonus = input.parseBonus(line);
                return new WinningNumbers(wins, bonus); // 중복/범위 검증 포함
            } catch (IllegalArgumentException e) {
                output.printError(e.getMessage());
            }
        }
    }

    private int pickTempBonus(List<Integer> wins) {
        for (int b = 1; b <= 45; b++) {
            if (!wins.contains(b)) return b;
        }
        // 이론상 도달 불가(6개만 채워짐). 방어적 코드.
        throw new IllegalStateException("[ERROR] 보너스 번호를 찾을 수 없습니다.");
    }
}