package dummy.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MVCController {

    @GetMapping("/delay")
    void delay3Secs() {
        System.out.println("before");
        delay("/delay", 3);
        System.out.println("after");
    }


    void delay(String path, int sec) {
        for (int i = 0; i < sec; i++) {
            System.out.println("http://localhost:81" + path + " -> " + (i + 1) + " second");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
