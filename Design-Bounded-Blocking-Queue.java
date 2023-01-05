class BoundedBlockingQueue {

    private int size;
    private List<Integer> lst;
    private Object lock;

    public BoundedBlockingQueue(int capacity) {
        this.size = capacity;
        this.lst = new LinkedList<>();
        this.lock = new Object();
    }
    
    public void enqueue(int element) throws InterruptedException {
         synchronized(lock) {
             while (lst.size() == size) {
                 lock.wait();
             }

             lst.add(element);
             lock.notifyAll();
         }
    }
    
    public int dequeue() throws InterruptedException {
        synchronized(lock) {
            while (lst.isEmpty()) {
                lock.wait();
            }

            int result = lst.get(0);
            lst.remove(0);
            lock.notify();

            return result;
        }
    }
    
    public int size() {
        return this.lst.size();
    }
}
