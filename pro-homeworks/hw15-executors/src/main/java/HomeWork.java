import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

@Slf4j
public class HomeWork {
    private static int nextMove = 1;

    public static void main(String[] args) {
        log.info("Starting homeWork");
        HomeWork homeWork = new HomeWork();
        new Thread(() -> homeWork.action(1, 1, 10)).start();
        new Thread(() -> homeWork.action(2, 1, 10)).start();
    }

    private synchronized void action(int threadId, int startValue, int endValue) {
        int counter = 1;
        boolean accending = true;
        while (!Thread.currentThread()
                      .isInterrupted()) {
            try {
                while (nextMove != threadId) {
                    this.wait();
                }
                log.info("Thread {}: {}", threadId, counter);
                if (accending) {
                    counter++;
                } else {
                    counter--;
                }
                if (counter == endValue || counter == startValue) {
                    accending = !accending;
                }
                nextMove = threadId == 1 ? 2 : 1;
                sleep(1000);
                notifyAll();
            } catch (InterruptedException e) {
                Thread.currentThread()
                      .interrupt();
            }
        }

    }
}
