import java.util.concurrent.Semaphore;

public class Philosophers {
    static int N = 5;

    static Semaphore[] chopsticks = new Semaphore[N];

    static class Philosopher extends Thread {
        int id;

        Philosopher(int id) {
            this.id = id;
        }

        public void run() {
            while (true) {
                try {
                    // Think
                    Thread.sleep((int) (Math.random() * 1000));

                    // Pick up chopsticks
                    chopsticks[id].acquire();
                    chopsticks[(id + 1) % N].acquire();

                    // Eat
                    System.out.println("Philosopher " + id + " is eating");
                    Thread.sleep((int) (Math.random() * 1000));

                    // Put down chopsticks
                    chopsticks[id].release();
                    chopsticks[(id + 1) % N].release();
                } catch (InterruptedException e) {
                    System.out.println("Philosopher " + id + " was interrupted");
                }
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < N; i++) {
            chopsticks[i] = new Semaphore(1);
        }

        for (int i = 0; i < N; i++) {
            new Philosopher(i).start();
        }
    }
}
