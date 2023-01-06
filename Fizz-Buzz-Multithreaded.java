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
        lock.lock();
        while (counter <= n) {
            if (counter % 3 != 0 || counter % 5 == 0) {
                conditionMet.await();
                continue;
            }
            printFizz.run();
            counter++;
            conditionMet.signalAll();
        }
        lock.unlock();
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        lock.lock();
        while (counter <= n) {
            if (counter % 5 != 0 || counter % 3 == 0) {
                conditionMet.await();
                continue;
            }
            printBuzz.run();
            counter++;
            conditionMet.signalAll();
        }
        lock.unlock();
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        lock.lock();
        while (counter <= n) {
            if (counter % 15 != 0) {
                conditionMet.await();
                continue;
            }
            printFizzBuzz.run();
            counter++;
            conditionMet.signalAll();
        }
        lock.unlock();
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        while (counter <= n) {
            if (counter % 3 == 0 || counter % 5 == 0) {
                conditionMet.await();
                continue;
            }

            printNumber.accept(counter++);
            conditionMet.signalAll();
        }
        lock.unlock();
    }
}
