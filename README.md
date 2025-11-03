#  java-lotto-precourse

## 1. 요구 사항 요약

* **로또 번호 범위:** 1 ~ 45
* **1장 구성:** 중복되지 않는 6개의 번호 (오름차순 출력)
* **로또 가격:** 1,000원 / 1장
* **입력:**
  * 구입 금액 (1,000원 단위)
  * 당첨 번호 (6개)
  * 보너스 번호 (1개)
* **출력:**
  * 발행된 로또 번호 목록
  * 당첨 통계 (1등 ~ 5등)
  * 총 수익률 (소수점 둘째 자리에서 반올림)
* **등수:**
  * **1등:** 6개 번호 일치 (2,000,000,000원)
  * **2등:** 5개 번호 + 보너스 번호 일치 (30,000,000원)
  * **3등:** 5개 번호 일치 (1,500,000원)
  * **4등:** 4개 번호 일치 (50,000원)
  * **5등:** 3개 번호 일치 (5,000원)
* **예외 처리:** 잘못된 입력 시 `[ERROR]` 메시지 출력 후 해당 부분부터 다시 입력받습니다.
* **테스트:** JUnit5와 AssertJ를 사용하며, UI 로직을 제외한 도메인 및 서비스 로직을 우선 테스트합니다.

---

## 2. 아키텍처

### 구조: MVC-Lite + Ports & Adapters

애플리케이션은 MVC-Lite 패턴을 기반으로, 도메인의 순수성을 유지하기 위해 포트와 어댑터(Hexagonal Architecture) 개념을 일부 도입합니다. 계층별 책임은 다음과 같습니다.

* **Config (in Application):** `main` 메서드가 위치하며, 각 계층의 객체(Controller, Service, View 등)를 생성하고 의존성을 주입(조립)하는 역할을 담당합니다.
* **Controller:** 사용자의 입력 흐름을 제어합니다. `InputView`로부터 입력을 받아 `Service`에 처리를 위임하고, 그 결과를 `OutputView`로 전달하여 출력을 요청합니다.
* **View (`InputView` / `OutputView`):** 사용자의 콘솔 입출력을 전담하는 경계 계층입니다.
* **Service:** 로또 발행, 당첨 판정, 수익률 계산 등 핵심 비즈니스 로직의 실행을 담당합니다. `Domain` 객체들을 조합하여 애플리케이션의 요구사항을 수행합니다.
* **Domain:** 순수한 비즈니스 규칙과 데이터(값 객체, 일급 컬렉션)를 포함합니다.
  * *주요 구성 요소:* `Lotto`, `LottoNumber`, `Tickets`, `WinningNumbers`, `Rank`, `Money`, `Yield`
* **Port & Adapter:** 외부 세계(랜덤 숫자 생성)와의 의존성을 분리합니다.
  * **Port (Interface):** `NumberPicker` 인터페이스가 `Service`가 필요로 하는 기능을 정의합니다.
  * **Adapter (Implementation):** `RandomsNumberPicker`가 `NumberPicker` 인터페이스를 구현하며, `missionutils.Randoms` 라이브러리를 실제 사용합니다. `Service`는 `Randoms`가 아닌 `NumberPicker` 인터페이스에만 의존합니다.

### 핵심 원칙

* **경계 분리:** 입출력(View)과 도메인 로직(Domain)을 분리합니다.
* **외부 의존성 분리:** 랜덤 숫자 생성 로직을 `Port`(`NumberPicker`)로 추상화하고, `Adapter`(`RandomsNumberPicker`)를 주입받아 사용합니다.
* **도메인 순수성:** 도메인 객체는 `Console`이나 `Randoms` 같은 외부 라이브러리에 직접 의존하지 않습니다.
* **일급 컬렉션:** `Tickets` (Lotto 리스트), `LottoNumbers` (Lotto 내부 숫자 리스트)를 사용합니다.
* **값 객체 (VO):** `LottoNumber`, `Money`를 값 객체로 다루어 불변성을 보장합니다.
* **Enum 활용:** `Rank` Enum이 당첨 조건(일치 개수, 보너스 필요 여부)과 상금 정보를 모두 갖도록 합니다.

---

## 3. 기능 목록 (Given-When-Then)

| 구분 | Given (주어진 상황) | When (이벤트 발생) | Then (결과) |
| :--- | :--- | :--- | :--- |
| **금액 입력** | 유저가 8000을 입력 | 구입 금액이 1000의 배수이면 | 8장의 로또가 발행된다. |
| **금액 검증** | 유저가 750을 입력 | 1000의 배수가 아니면 | `[ERROR]` 출력 후 재입력 받는다. |
| **로또 생성** | 1장의 로또를 발행할 때 | 1~45 범위에서 중복 없는 6개 숫자를 | 오름차순으로 정렬된 리스트를 반환한다. |
| **당첨 입력** | 유저가 “1,2,3,4,5,6”을 입력 | 입력 형식이 유효하면 | `WinningNumbers` 객체를 생성한다. |
| **보너스 입력** | 유저가 “7”을 입력 | 당첨 번호와 중복되지 않으면 | 보너스 번호를 등록한다. |
| **당첨 판정** | 로또 1장과 당첨 번호를 비교하여 | 6개 번호가 모두 일치하면 | `Rank.FIRST` (1등)을 반환한다. |
| **당첨 판정** | 로또 1장과 당첨 번호를 비교하여 | 5개 번호 + 보너스 번호가 일치하면 | `Rank.SECOND` (2등)을 반환한다. |
| **당첨 판정** | 로또 1장과 당첨 번호를 비교하여 | 3개 번호가 일치하면 | `Rank.FIFTH` (5등)을 반환한다. |
| **당첨 통계** | 구매한 모든 로또와 당첨 번호를 비교 | 당첨 판정을 완료하면 | 등수별 당첨 개수를 집계하여 출력한다. |
| **수익률 계산** | (총상금 ÷ 총 구매액) * 100 | 수익률을 계산할 때 | 소수점 둘째 자리에서 반올림된 % 값 (예: 62.5%)을 출력한다. |

---

## 4. 예외 정책 (결정표)

| 구분 | 조건 | 예외 타입 | 에러 메시지 |
| :--- | :--- | :--- | :--- |
| **구입 금액** | 1000원 단위가 아님 / 0 이하 | `IllegalArgumentException` | `[ERROR] 구입 금액은 1,000원 단위의 양수여야 합니다.` |
| **로또 번호** | 6개 미만 또는 6개 초과 | `IllegalArgumentException` | `[ERROR] 로또 번호는 6개여야 합니다.` |
| **로또 번호** | 1~45 범위를 위반 | `IllegalArgumentException` | `[ERROR] 로또 번호는 1부터 45 사이의 숫자여야 합니다.` |
| **로또 번호** | 중복된 숫자가 존재 | `IllegalArgumentException` | `[ERROR] 로또 번호에 중복이 있습니다.` |
| **당첨 번호** | 입력 포맷 오류 (쉼표, 개수) | `IllegalArgumentException` | `[ERROR] 당첨 번호는 쉼표(,)로 구분된 6개의 숫자여야 합니다.` |
| **보너스 번호** | 1~45 범위를 위반 | `IllegalArgumentException` | `[ERROR] 보너스 번호는 1부터 45 사이의 숫자여야 합니다.` |
| **보너스 번호** | 당첨 번호와 중복됨 | `IllegalArgumentException` | `[ERROR] 보너스 번호는 당첨 번호와 중복될 수 없습니다.` |

---

## 5. 테스트 계획 (레이어별)

| 레이어 | 테스트 대상 | 주요 검증 내용 |
| :--- | :--- | :--- |
| **Domain** | `LottoNumber` | 1~45 범위 검증, `equals` / `hashCode` 동작 |
| **Domain** | `Lotto` | 6개 개수 검증, 번호 중복 검증, 자동 정렬 |
| **Domain** | `WinningNumbers` | 당첨 번호와 보너스 번호 간의 중복 검증, 범위 검증 |
| **Domain** | `Rank` | (일치 개수, 보너스 일치 여부) 조합에 따른 정확한 `Rank` 반환 |
| **Domain** | `Yield` / `Money` | 수익률 계산 시 `BigDecimal`의 `HALF_UP` 반올림 규칙 적용 |
| **Service** | `LottoService` | 로또 발행, 당첨 판정, 통계 집계, 수익률 계산 통합 흐름 |
| **Port** | `NumberPicker` | (Test Double) 고정된 값을 반환하는 전략 테스트 / 랜덤 전략 테스트 |
| **UI** | `InputView` / `OutputView` | (통합 테스트) 예외 발생 시 재입력 루프 동작, 최종 출력 포맷 |

**테스트 조직화 노트:** 제공된 `LottoTest`는 그대로 유지하고, 보강 케이스는 별도 파일 `LottoValidationTest`로 추가한다.
---

## 6. 구현 및 커밋 순서 (TDD)

1.  `docs(readme)`: 요구 사항, 구조, 규칙 정리
2.  `test(domain)`: `LottoNumber` 범위 검증 (1~45)
3.  `feat(domain)`: `LottoNumber` 값 객체 구현
4.  `test(domain)`: `Lotto` 생성 시 6개, 중복 불가, 자동 정렬 테스트
5.  `feat(domain)`: `Lotto` 클래스 및 유효성 검증 완성
6.  `test(domain)`: `Rank` Enum (일치 개수/보너스) 매칭 테스트
7.  `feat(domain)`: `Rank` Enum 구현 (상금, 매칭 로직 포함)
8.  `test(domain)`: `Yield` 수익률 계산 및 소수점 반올림 테스트
9.  `feat(domain)`: `Yield`, `Money` 값 객체 구현
10. `feat(port)`: `NumberPicker` 인터페이스 및 `RandomsNumberPicker` 어댑터 구현
11. `test(service)`: `LottoService` 로또 발행, 판정, 통계 통합 테스트
12. `feat(service)`: `LottoService` 구현
13. `feat(view)`: `InputView`, `OutputView` 구현 및 예외 처리 재시도 루프 적용
14. `refactor(domain)`: `Tickets` 일급 컬렉션 도입 및 리팩토링

---

## 7. 제약 및 의존성

* **JDK:** 21
* **라이브러리:** `camp.nextstep.edu.missionutils` (Console, Randoms)
* **금지 사항:**
  * `System.exit()` 사용 금지
  * `switch/case` 문, `else` 문 사용 지양 (다형성 또는 Enum 활용)
  * `missionutils` 외 외부 라이브러리 추가 금지
* **함수 제약:**
  * Indent(들여쓰기) 2 depth 이하
  * 함수(메서드) 라인 15라인 이하
  * 단일 책임 원칙(SRP) 준수
* **테스트:** UI 로직(`Console`, `Randoms` 직접 호출)은 테스트에서 제외
* **출력:** 제공된 입출력 예시와 출력 포맷이 완전히 일치해야 함
* **반올림:** 수익률 계산 시 `BigDecimal`의 `HALF_UP` 모드를 사용하여 소수점 둘째 자리에서 반올림
* **패키지:** `lotto` 패키지를 기준으로 하위 패키지 구조는 자유롭게 변경 가능 (단, `Lotto` 클래스의 필드 수정 금지)