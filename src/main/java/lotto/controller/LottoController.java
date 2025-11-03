package lotto.controller;

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
        Money amount = readAmountWithRetry();
        Tickets tickets = new Tickets(service.issueTickets(amount));
        output.printTickets(tickets);

        WinningNumbers winning = readWinningWithRetry();
        int bonus = readBonusWithRetry();

        winning = new WinningNumbers(winningNumbers(winning), bonus);
        ServiceResult result = service.judgeAll(tickets.asList(), winning, amount);
        output.printStatistics(result);
    }

    private Money readAmountWithRetry() {
        while (true) {
            try {
                String line = input.prompt("구입금액을 입력해 주세요.");
                long won = Long.parseLong(line.trim());
                return Money.of(won);
            } catch (Exception e) {
                output.printError(errorOrWrap(e, "[ERROR] 구입 금액은 1,000원 단위의 양수여야 합니다."));
            }
        }
    }

    private WinningNumbers readWinningWithRetry() {
        while (true) {
            try {
                System.out.println();
                String line = input.prompt("당첨 번호를 입력해 주세요.");
                return new WinningNumbers(input.parseWinningNumbers(line), 1); // 임시 보너스(아래에서 대체)
            } catch (IllegalArgumentException e) {
                output.printError(e.getMessage());
            } catch (Exception e) {
                output.printError("[ERROR] 당첨 번호는 쉼표(,)로 구분된 6개의 숫자여야 합니다.");
            }
        }
    }

    private int readBonusWithRetry() {
        while (true) {
            try {
                System.out.println();
                String line = input.prompt("보너스 번호를 입력해 주세요.");
                return input.parseBonus(line);
            } catch (IllegalArgumentException e) {
                output.printError(e.getMessage());
            } catch (Exception e) {
                output.printError("[ERROR] 보너스 번호는 1부터 45 사이의 숫자여야 합니다.");
            }
        }
    }

    private List<Integer> winningNumbers(WinningNumbers temp) {
        // temp는 보너스 검증을 위해 임시 생성. 실제 번호만 꺼내 쓰기 위한 헬퍼 필요 없으면 WinningNumbers 생성 시 바로 처리해도 됨.
        // 여기서는 간결성을 위해 리플렉션 없이 생성자 인자만 재사용.
        // WinningNumbers 내부 구조를 노출하지 않기 위해 입력에서 다시 파싱해도 무방.
        return input.parseWinningNumbers(String.join(",", tempNumbersString(temp)));
    }

    private String[] tempNumbersString(WinningNumbers temp) {
        // 편의용: 다시 파싱할 필요 없게 Output 없이 내부를 꺼내는 메서드는 만들지 않는다(캡슐화 유지).
        throw new UnsupportedOperationException("컨트롤러 단순화를 위해 WinningNumbers를 바로 생성하세요.");
    }

}