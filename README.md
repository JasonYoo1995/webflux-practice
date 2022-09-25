## 본 레포의 특징

- Reactor와 Spring Webflux에 처음 입문하는 사람 입장에서, 공식 문서만 보고는 실무 코드 작성에 필요한 내부적인 동작에 대한 이해나 노하우를 얻기 어렵다.
- 본 레포는 그러한 내용들에 대해 빠르고 효율적으로 이해할 수 있도록 주요 케이스들에 대해 예제 및 주석으로 설명해놓았으며, 가장 자주 쓰이는 케이스 위주로 담았다.
- Reactor Practice의 경우, 가장 자주 쓰이는 연산자(Operator)들로 Publisher를 정의하고, 해당 Publisher를 구독할 때 발생하는 현상과 주의 사항에 대해 담았다.
- Webflux Practice의 경우, 실무에서 경험했던 요구 사항과 코드 패턴을 토대로 비슷한 요구 사항의 예제를 만들었다.
- Spring Webflux 서버가 외부 API를 실제로 호출했을 때 발생하는 문제와 고려사항들을 실제와 동일하게 시뮬레이션할 수 있도록, 총 2개의 서버(Webflux 서버 + 외부 API 서버)를 레포에 생성해놓았으며, 외부 API 서버는 실험의 편의를 위해 멀티 스레드 기반의 Spring MVC 서버로 구성하였다.
- 잘못된 내용은 누구나 고쳐주시길 바라고, 부족한 내용도 누구나 보충해주시길 바라며, 추가적으로 실험해보고 싶은 내용도 누구나 코드를 추가하여 실험해보시길 바란다.

## 사용법
- 프로젝트 구조
  - 1개의 상위 프로젝트가 3개의 하위 프로젝트(ReactorPractice, SpringWebflux, SpringMVC)를 포함하고 있다.
- ReactorPractice 프로젝트
  - Reactor의 Mono와 Flux에 대해 이해하는 프로젝트이며, 실행하고자 하는 메서드만 Main 메서드 Body에 작성해서 Main 메서드를 실행하면 된다.
- SpringWebflux 프로젝트
  - 실험하고자 하는 Webflux 서버이며, localhost 80번 포트에서 구동된다. 
- SpringMVC 프로젝트
  - Webflux 서버가 호출할 외부 API 서버로, localhost 81번 포트에서 구동된다.
- 구동 및 실행
  - 2개의 서버를 로컬에 동시에 구동한다.
  - 본 프로젝트의 최상위 경로에 있는 postman_collection.json 파일을 Postman에 import하여 API를 호출한다.
- 결과 모니터링
  - 2개의 로그 콘솔을 동시에 확인하면서, Webflux의 동작 방식을 이해한다.