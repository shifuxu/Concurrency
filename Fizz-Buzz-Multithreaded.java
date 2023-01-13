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


// easier solution with Semaphore
class FizzBuzz {
    private int n;
    private Semaphore fizz;
    private Semaphore buzz;
    private Semaphore fizzBuzz;
    private Semaphore number;

    public FizzBuzz(int n) {
        this.n = n;
        // init the top 3 with 0 permits, so it will wait until been released by previous number.
        fizz = new Semaphore(0);
        buzz = new Semaphore(0);
        fizzBuzz = new Semaphore(0);
        number = new Semaphore(1);
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            if (i % 3 == 0 && i % 5 != 0) {
                fizz.acquire();
                printFizz.run();
                releaseLock(i + 1);
            }
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            if (i % 3 != 0 && i % 5 == 0) {
                buzz.acquire();
                printBuzz.run();
                releaseLock(i + 1);
            }
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            if (i % 3 == 0 && i % 5 == 0) {
                fizzBuzz.acquire();
                printFizzBuzz.run();
                releaseLock(i + 1);
            }
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            if (i % 3 != 0 && i % 5 != 0) {
                number.acquire();
                printNumber.accept(i);
                releaseLock(i + 1);
            }
        }
    }
    
    // key idea, release the lock with i + 1 to determine so that we can maintain the order.
    private void releaseLock(int k) {
        if (k % 3 == 0 && k % 5 != 0) {
            fizz.release();
        } else if (k % 3 != 0 && k % 5 == 0) {
            buzz.release();
        } else if (k % 3 == 0 && k % 5 == 0) {
            fizzBuzz.release();
        } else {
            number.release();
        }
    }
}
