import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomBlockingQueue<Ticket> {
    private final Queue<Ticket> queue;
    private final int capacity;
    private final ReadWriteLock lock;
    private final Condition notEmpty;
    private final Condition notFull;

    public CustomBlockingQueue(int capacity) {
        this.queue = new LinkedList<>();
        this.capacity = capacity;
        this.lock = new ReentrantReadWriteLock(true);
        this.notEmpty = lock.writeLock().newCondition();
        this.notFull = lock.writeLock().newCondition();
    }

    public void put(Ticket ticket) throws InterruptedException {
        if (ticket == null) throw new NullPointerException();

        lock.writeLock().lock();
        try {
            while (queue.size() == capacity) {
                notFull.await();
            }
            queue.add(ticket);
            notEmpty.signal();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Ticket take() throws InterruptedException {
        lock.writeLock().lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            Ticket result = queue.remove();
            notFull.signal();
            return result;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int size() {
        lock.readLock().lock();
        try {
            return queue.size();
        } finally {
            lock.readLock().unlock();
        }
    }
}
