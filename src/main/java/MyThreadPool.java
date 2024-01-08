import java.util.LinkedList;
import java.util.List;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyThreadPool   {


    public class Worker implements Runnable{


        public AtomicBoolean flag = new AtomicBoolean(false);

        public Worker(int threaddId) {

        }




        @Override
        public void run() {
            try {
                workerRoutine();
                System.out.println();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void workerRoutine() throws InterruptedException {

while(isRunning) {
    synchronized (tasks) {
        Runnable task = tasks.take();
        task.run();
            }
          }
        }


    }


    private List<Worker> workers = new LinkedList<>();
    private final int poolSize;
    private volatile boolean isRunning = true;
    private ReentrantLock mutex = new ReentrantLock();
    private Condition not_empty = mutex.newCondition();

     MyBlockingQueue<Runnable> tasks = new MyBlockingQueue<>();

    public MyThreadPool(int poolSize) throws InterruptedException {
        this.poolSize = poolSize;
        startWorkerThreads(poolSize);
    }

    public void submit(Runnable task) throws InterruptedException {
        tasks.put(task);
    }


    public void waitPool() throws InterruptedException {
        mutex.lock();
        try {
            while (isRunning) {
                not_empty.await();
            }
        } finally {
            mutex.unlock();
        }
    }


    public void stop(){
        isRunning = false;
        for( Worker worker: workers){
            worker.flag.set(false);
        }
    }




    public void startWorkerThreads(int num) throws InterruptedException {
      for (int i = 0; i < num; i++) {

          Worker worker = new Worker(i);
         workers.add(worker);
          new Thread(worker).start();

      }
  }
}
