class ZeroEvenOdd {
    private int n;
    private Lock lock = new ReentrantLock();
    private boolean shouldPrintZero = true;
    private boolean shouldPrintEven = false;
    private boolean shouldPrintOdd = false;
    private Condition printZero = lock.newCondition();
    private Condition printEven = lock.newCondition();
    private Condition printOdd = lock.newCondition();
    private int counter = 0;
    
    public ZeroEvenOdd(int n) {
        this.n = n;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            lock.lock();
            if (!shouldPrintZero) {
                printZero.await();
            }

            printNumber.accept(0);
            if ((i & 1) == 1) {
                shouldPrintOdd = true;
                printOdd.signal();
            } else {
                shouldPrintEven = true;
                printEven.signal();
            }

            shouldPrintZero = false;
            lock.unlock();
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            lock.lock();
            if (!shouldPrintEven) {
                printEven.await();
            }

            printNumber.accept(i);

            shouldPrintEven = false;
            shouldPrintZero = true;
            printZero.signal();
            lock.unlock();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            lock.lock();
            if (!shouldPrintOdd) {
                printOdd.await();
            }

            printNumber.accept(i);

            shouldPrintOdd = false;
            shouldPrintZero = true;
            printZero.signal();

            lock.unlock();
        }
    }
}
