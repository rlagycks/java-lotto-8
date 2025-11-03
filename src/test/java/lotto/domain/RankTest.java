package lotto.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RankTest {

    @DisplayName("일치 개수와 보너스 여부로 등수를 결정한다")
    @ParameterizedTest(name = "match={0}, bonus={1} => {2}")
    @CsvSource({
            "6,false,FIRST",
            "5,true,SECOND",
            "5,false,THIRD",
            "4,false,FOURTH",
            "3,false,FIFTH",
            "2,false,MISS",
            "0,false,MISS"
    })
    void 매핑규칙(int matchCount, boolean bonusMatched, Rank expected) {
        assertThat(Rank.of(matchCount, bonusMatched)).isEqualTo(expected);
    }

    @DisplayName("보너스는 5개 일치일 때만 영향을 준다")
    @ParameterizedTest(name = "match={0}, bonus={1} => {2}")
    @CsvSource({
            "4,true,FOURTH",
            "3,true,FIFTH",
            "6,true,FIRST"
    })
    void 보너스_무시_및_예외케이스(int matchCount, boolean bonusMatched, Rank expected) {
        assertThat(Rank.of(matchCount, bonusMatched)).isEqualTo(expected);
    }
}