
public class Main {
    public static void main(String[] args) throws InterruptedException {
        MyThreadPool myThreadPool = new MyThreadPool(5);

        for (int i = 0; i < 10; i++) {
            int num_task = i;
            myThreadPool.submit(() -> {
                System.out.println("Task " + num_task + " executed  "
                    );
            });
        }

        myThreadPool.waitPool();
        myThreadPool.stop();
    }
}