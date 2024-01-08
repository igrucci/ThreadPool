import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
//
//public class MyBlockingQueue<T> {
//
//    private Deque<T> buffer;
//    private ReentrantLock mutex = new ReentrantLock();
//    private Condition not_empty = mutex.newCondition();
//
//    public MyBlockingQueue() {
//        this.buffer = new ConcurrentLinkedDeque<>();
//    }
//
//    // thread role: producer
//    public void put(T value) {
//        mutex.lock();
//        try {
//            buffer.add(value);
//            not_empty.signal();
//        } finally {
//            mutex.unlock();
//        }
//    }
//
//    // thread role consumer
//    public T take() throws InterruptedException {
//        mutex.lock();
//        try {
//            while (buffer.isEmpty()) {
//                not_empty.await();
//            }
//            return takeLocked();
//        } finally {
//            mutex.unlock();
//        }
//    }
//
//    private T takeLocked() {
//        mutex.lock();
//        try {
//            assert !buffer.isEmpty();
//            T front = buffer.removeFirst();
//            return front;
//        } finally {
//            mutex.unlock();
//        }
//    }
//}

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// Unbounded Multi-Producer / Multi-Consumer (MPMC) Blocking Queue

public class MyBlockingQueue<T> {

    private Deque<T> buffer;
    private ReentrantLock mutex = new ReentrantLock();
    private Condition not_empty = mutex.newCondition();
    public MyBlockingQueue() {
        this.buffer = new ConcurrentLinkedDeque<>();
    }

    // thread role: producer
    public void put(T value) {
        mutex.lock();
        try {
            buffer.add(value);
      //      not_empty.notify();
        } finally {
            mutex.unlock();
        }
    }
    // thread role consumer
    public T take() throws InterruptedException {


        mutex.lock();
        try {
            while (buffer.isEmpty()) {
                try {
                    // Release the lock before sleeping
                    mutex.unlock();
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    // Reacquire the lock before checking the loop condition
                    mutex.lock();
                }
            }
            return takeLocked();
        } finally {
            mutex.unlock();
        }

    }
        private T takeLocked() {
        mutex.lock();
        try {
            // Проверяем, что очередь не пуста
            assert !buffer.isEmpty();
            // Перемещаем первый элемент очереди в переменную front
            T front = buffer.removeFirst();
            // Возвращаем значение front
            return front;
        } finally {
            mutex.unlock();
        }
    }
}


//        mutex.lock();
//        try {
//            while (buffer.isEmpty()) {
//                try {
//                    // Release the lock before sleeping
//                    mutex.unlock();
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                } finally {
//                    // Reacquire the lock before checking the loop condition
//                    mutex.lock();
//                }
//            }
//            return takeLocked();
//        } finally {
//            mutex.unlock();
//        }
