package codes.shubham.patterns;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class ProducerConsumer {
    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();
        final Generator generator = new Generator();

        Thread producer = new Thread(() -> {
            while (generator.getLastGenerated() <= 30) {
                while (queue.size() > 7) {
                    synchronized (queue) {
                        try {
                            System.out.println("Queue already has "+ queue.size() + " elements. Waiting for consumer to consume.");
                            queue.notifyAll();
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        queue.notifyAll();
                    }
                }
                System.out.println("Queue has space now. Resuming production.");

                List<Integer> values = generator.generate(10);

                synchronized (queue) {
                    queue.addAll(values);
                    System.out.println("Produced: " + values);
                    queue.notifyAll();
                }
            }

        });

        producer.start();

        Thread consumer = new Thread(() -> {
           while (true) {
               while (queue.isEmpty()) {
                   synchronized (queue) {
                       try {
                           queue.notifyAll();
                           queue.wait();
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                    }
                }

               Integer value = 0;
                synchronized (queue) {
                    value = queue.poll();
                    queue.notifyAll();
                    try {
                        Thread.sleep(4000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
               System.out.println("Consumed: " + value);
                if (value == 30) break;
           }
        });

        consumer.start();
    }
}

class Generator {
    private int lastGenerated = 0;

    // Generating values takes time
    public List<Integer> generate(int n) {
        Random random = new Random();
        List<Integer> result = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            result.add(getNext());
            try {
                Thread.sleep(random.nextInt(1000, 2000));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    private int getNext() {
        return lastGenerated++;
    }

    public int getLastGenerated() {
        return lastGenerated;
    }
}
