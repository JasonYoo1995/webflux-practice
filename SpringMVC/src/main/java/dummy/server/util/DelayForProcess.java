package dummy.server.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DelayForProcess {
    public static void delay(String path, int sec) {
        for (int i = 0; i < sec; i++) {
            log.info("http://localhost:81" + path + " -> " + (i + 1) + " second");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
