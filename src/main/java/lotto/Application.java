package lotto;

import lotto.adapter.RandomsNumberPicker;
import lotto.controller.LottoController;
import lotto.service.LottoService;
import lotto.view.InputView;
import lotto.view.OutputView;

public class Application {
    public static void main(String[] args) {
        InputView input = new InputView();
        OutputView output = new OutputView();
        LottoService service = new LottoService(new RandomsNumberPicker());

        try {
            new LottoController(input, output, service).run();
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}