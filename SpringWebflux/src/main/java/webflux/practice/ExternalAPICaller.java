package webflux.practice;

public class ExternalAPICaller {
    /**
     * [Webflux에서 RestTemplate를 쓰면 안 되는 이유]
     * Webflux는 싱글 스레드이기 때문에
     * RestTemplate을 통해 외부 API에 요청하고 응답이 올때까지의 시간 동안 스레드가 Blocking되면
     * 우리 서버 내부의 다른 동작을 전혀 수행하지 못하고 오랫동안 자원을 낭비하게 된다
     * 즉, Spring MVC의 '멀티 스레드 + Blocking' 방식보다도 훨씬 느린 성능을 초래한다
     * WebClient의 Non-blocking Call 방식은
     * 외부 API의 요청 직후, 우리 서버 내부의 다른 동작을 수행하다가
     * 외부 API의 응답 시 Event Loop에 의해 Callback을 수행함으로써
     * 자원 이용률을 최대한 끌어올릴 수 있게 되고
     * 따라서 이러한 '싱글 스레드 + Non-blocking' 방식은 '멀리 스레드 + Blocking' 방식보다 높은 성능을 보여준다
     */
}
