import java.util.concurrent.Semaphore;

public class Philosophers4 {
    static int N;
    static Semaphore[] chopsticks;
    static Semaphore mutex;
    static boolean[] eating;

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

                    // Check if neighbors are eating
                    mutex.acquire();
                    while (eating[(id + N - 1) % N] || eating[(id + 1) % N]) {
                        mutex.release();
                        Thread.sleep((int) (Math.random() * 1000));
                        mutex.acquire();
                    }
                    eating[id] = true;
                    mutex.release();

                    // Pick up chopsticks
                    chopsticks[id].acquire();
                    chopsticks[(id + 1) % N].acquire();

                    // Eat
                    System.out.println("Philosopher " + id + " is eating");
                    Thread.sleep((int) (Math.random() * 1000));

                    // Put down chopsticks
                    chopsticks[id].release();
                    chopsticks[(id + 1) % N].release();

                    // Finish eating
                    mutex.acquire();
                    eating[id] = false;
                    mutex.release();
                } catch (InterruptedException e) {
                    System.out.println("Philosopher " + id + " was interrupted");
                }
            }
        }
    }

    public static void main(String[] args) {
        N = 10;
        chopsticks = new Semaphore[N];
        mutex = new Semaphore(1);
        eating = new boolean[N];

        for (int i = 0; i < N; i++) {
            chopsticks[i] = new Semaphore(1);
        }

        for (int i = 0; i < N; i++) {
            new Philosopher(i).start();
        }
    }
}
