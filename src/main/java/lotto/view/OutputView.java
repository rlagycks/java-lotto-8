package lotto.view;

import java.util.List;

import lotto.domain.Lotto;
import lotto.domain.Rank;
import lotto.domain.Yield;
import lotto.service.ServiceResult;
import lotto.tickets.Tickets;

public final class OutputView {

    public void printTickets(Tickets tickets) {
        System.out.println(tickets.size() + "개를 구매했습니다.");
        for (Lotto t : tickets.asList()) {
            List<Integer> nums = t.getNumbers();
            System.out.println(nums.toString());
        }
        System.out.println();
    }

    public void printStatistics(ServiceResult result) {
        System.out.println("당첨 통계");
        System.out.println("---");
        System.out.println("3개 일치 (5,000원) - " + result.countOf(Rank.FIFTH) + "개");
        System.out.println("4개 일치 (50,000원) - " + result.countOf(Rank.FOURTH) + "개");
        System.out.println("5개 일치 (1,500,000원) - " + result.countOf(Rank.THIRD) + "개");
        System.out.println("5개 일치, 보너스 볼 일치 (30,000,000원) - " + result.countOf(Rank.SECOND) + "개");
        System.out.println("6개 일치 (2,000,000,000원) - " + result.countOf(Rank.FIRST) + "개");
        Yield y = result.yield();
        System.out.println("총 수익률은 " + y.asPercent() + "입니다.");
    }

    public void printError(String message) {
        System.out.println(message);
    }
}