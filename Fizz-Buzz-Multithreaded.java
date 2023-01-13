class FizzBuzz {
    private int n;
    private Lock lock;
    private Condition conditionMet;
    private int counter;

    public FizzBuzz(int n) {
        this.n = n;
        this.lock = new ReentrantLock();
        this.conditionMet = lock.newCondition();
        this.counter = 1;
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        try {
            lock.lock();
            while (counter <= n) {
                if (counter % 3 != 0 || counter % 5 == 0) {
                    conditionMet.await();
                    continue; // this is key point, after count increased, need to check again.
                }

                printFizz.run();
                counter++;
                conditionMet.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        try {
            lock.lock();
            while (counter <= n) {
                if (counter % 3 == 0 || counter % 5 != 0) {
                    conditionMet.await();
                    continue; // this is key point, after count increased, need to check again.
                }

                printBuzz.run();
                counter++;
                conditionMet.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        try {
            lock.lock();
            while (counter <= n) {
                if (counter % 3 != 0 || counter % 5 != 0) {
                    conditionMet.await();
                    continue; // this is key point, after count increased, need to check again.
                }

                printFizzBuzz.run();
                counter++;
                conditionMet.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        try {
            lock.lock();
            while (counter <= n) {
                if (counter % 3 == 0 || counter % 5 == 0) {
                    conditionMet.await();
                    continue; // this is key point, after count increased, need to check again.
                }

                printNumber.accept(counter);
                counter++;
                conditionMet.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
}
