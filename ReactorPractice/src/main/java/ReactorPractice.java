import reactor.core.CorePublisher;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class ReactorPractice {
    public static void main(String[] args) {
        System.out.println("==============================================================");
//        practice1();
//        practice2();
        practice3();
//        practice4();
//        practice5();
        System.out.println("==============================================================");
    }

    /**
     * doOnXXX()는 보통 로그 남길 때 사용
     */
    static void practice1() {
        String string = "my-string";
        Mono<String> stringMono = Mono.just(string); // 시퀀스 생성

        System.out.println("Mono는 1개");
        stringMono.doOnNext(val -> {
            System.out.println(val);
        }).subscribe(); // 시퀀스 구독

        Flux<Integer> integerFlux = Flux.just(1, 2, 3); // 시퀀스 생성

        System.out.println("Flux는 여러 개");
        integerFlux.doOnNext(val -> {
            System.out.println(val);
        }).subscribe();

        stringMono.doOnNext(val -> {
            System.out.println(val + " : 여러 번 구독 가능");
        }).subscribe();
    }

    /**
     * map() : 가공
     */
    static void practice2() {
        Mono.just(1)
                .doOnNext(System.out::println) // 출력
                .map(val -> { // 가공
                    return val + 1;
                })
                .doOnNext(System.out::println)
                .map(val -> val + 1) // 가공
                .doOnNext(System.out::println)
                .map(val -> {
                    return minusOne(val);
                })
                .doOnNext(System.out::println)
                .map(ReactorPractice::minusOne)
                .doOnNext(System.out::println)
                .map(val -> {
                    val += 10;
                    int result = add(val, 100);
                    return result;
                })
                .doOnNext(System.out::println)
                .subscribe();
    }

    static int minusOne(int num) {
        return num - 1;
    }

    static int add(int num, int num2) {
        return num + num2;
    }

    /**
     * wrapping과 flattening
     */
    static void practice3() {
        Flux<String> seq1 = Flux.just("0", "1", "2");
        Mono<String> seq2 = Mono.just("3");
        Flux<String> seq3 = Flux.just("4", "5");

        /** (1) 이어진 시퀀스의 모습 확인 (wrapping한 것을 또 wrapping한 상태) */
        System.out.println("------------(1)------------");
        Flux<CorePublisher<String>> mergedFlux = Flux.just(seq1, seq2, seq3);// Mono와 Flux는 둘 다 CorePublisher라는 인터페이스를 구현했음
        mergedFlux.log().subscribe();
        // log()를 통해 출력된 결과를 보면, mergedFlux는 [Flux, Mono, Flux] 형태임을 알 수 있음
        // 즉, [ ["0", "1", "2"], ["3"], ["4", "5"] ] 형태임

        /** (2) 이어진 시퀀스의 모습 확인 (doOnXXX 이해하기) */
        System.out.println("------------(2)------------");
        mergedFlux.doOnEach(seq -> System.out.println(seq)).subscribe();
        System.out.println("=======");
        mergedFlux.doOnNext(seq -> System.out.println(seq)).subscribe();
        System.out.println("=======");
        mergedFlux.doOnComplete(() -> System.out.println("complete 이벤트 발생")).subscribe();

        /**
         * (3) Flux.flatMap()의 동작 방식
         * outer publisher는 유지하고, inner publisher를 flatten시킴
         */
        System.out.println("------------(3)------------");

        // wrapping을 2번 더 시켜 보자
        Flux<Mono<Flux<CorePublisher<String>>>> nestedFlux = // [ [ [ ["0", "1", "2"], ["3"], ["4", "5"] ] ] ]
                Flux.just(
                        Mono.just(
                                mergedFlux // [ ["0", "1", "2"], ["3"], ["4", "5"] ]
                        )
                );

        // flatMap으로 wrapping을 1개씩 제거해보자
        nestedFlux
                .flatMap(mono -> {
                    System.out.println("depth 1 : " + mono);
                    return mono;
                }) // [ [ ["0", "1", "2"], ["3"], ["4", "5"] ] ]
                .flatMap(flux -> {
                    System.out.println("depth 2 : " + flux);
                    return flux;
                }) // [ ["0", "1", "2"], ["3"], ["4", "5"] ]
                .flatMap(fluxOrMono -> {
                    System.out.println("depth 3 : " + fluxOrMono);
                    return fluxOrMono;
                }) // [ "0", "1", "2", "3", "4", "5" ]
                .doOnNext(string -> {
                    System.out.println("doOnNext() : " + string);
                }).subscribe();
    }

    /**
     * flatMap()은 병렬로 구독한다 (Webflux 서버 개발 시, 외부 API를 호출하는 코드를 작성할 때, 반드시 알아야 할 개념)
     */
    static void practice4() {
        System.out.println("[delay가 없는 경우]");
        Flux<Mono<Integer>> integerMonoFlux = Flux.just(Mono.just(0), Mono.just(1), Mono.just(2));
        Flux<Integer> integerFlux = integerMonoFlux.flatMap(integerMono -> integerMono);
        integerFlux.doOnNext(System.out::println).subscribe();

        System.out.println("=====");
        System.out.println("[delay를 준 경우]");

        Flux<Mono<Integer>> delayFlux = Flux.just(
                Mono.just(0).delayElement(Duration.ofSeconds(6)),
                Mono.just(1).delayElement(Duration.ofSeconds(4)),
                Mono.just(2).delayElement(Duration.ofSeconds(2))
        );
        Flux<Integer> newSequence = delayFlux.flatMap(integerMono -> integerMono);
        for (int num : newSequence.toIterable()) {
            System.out.println("실행 : " + num);
        }
        System.out.println("flatMap()이 delayFlux 내부의 3개의 Mono들을 동시에(=병렬로) 구독하면");
        System.out.println("delay(=처리 시간)이 짧은 것부터 먼저 newSequence에 삽입하므로");
        System.out.println("newSequence에는 [0, 1, 2]가 아닌 [2, 1, 0]라는 시퀀스가 만들어진다");

        Integer firstInteger = newSequence.blockFirst();
        System.out.println("firstInteger = " + firstInteger);
    }

    /**
     * subscribe()는 non-blocking이고
     * block()은 blocking이다
     */
    static void practice5() {
        printBySubscribe();
        printByBlock();

        System.out.println("---------");

        printByBlock();
        printBySubscribe();

        // subscribe()의 doOnNext()가 마저 출력될 때까지 기다리고 나서 practice5() 메서드 종료
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void printBySubscribe() { // (4)-(1)-(2)-(3) 순으로 출력
        Flux<String> fluxForSubscribe = Flux.just("                       (1)", "                       (2)", "                       (3)");
        Disposable disposable = fluxForSubscribe
                .delayElements(Duration.ofSeconds(2)) // 각 element마다 1초 동안의 delay 후에 consume 가능
                .doOnNext(System.out::println)
                .subscribe(); // subscribe()는 non-blocking이므로 disposable이 즉시 리턴된다
        System.out.println("                       (4) disposable = " + disposable);
    }

    static void printByBlock() { // <1>-<2>-<3>-<4> 순으로 출력
        Flux<String> fluxForBlock = Flux.just("<1>", "<2>", "<3>");
        String lastString = fluxForBlock
                .delayElements(Duration.ofSeconds(2)) // 각 element마다 1초 동안의 delay 후에 consume 가능
                .doOnNext(System.out::println)
                .blockLast(); // block()은 blocking이므로 lastString이 즉시 리턴되지 않고
        System.out.println("<4> lastString = " + lastString);
    }
}