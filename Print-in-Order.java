class Foo {

    private ReentrantLock lock;
    private boolean oneDone;
    private boolean twoDone;
    private Condition conditionOne;
    private Condition conditionTwo;

    public Foo() {
        this.lock = new ReentrantLock();
        this.oneDone = false;
        this.twoDone = false;
        this.conditionOne = lock.newCondition();
        this.conditionTwo = lock.newCondition();
    }

    public void first(Runnable printFirst) throws InterruptedException {
        lock.lock();
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
        oneDone = true;
        conditionOne.signal();
        lock.unlock();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        lock.lock();
        while (!oneDone) { // use this boolean incase first thread finished first, this case, we are not rely on await to wake up
            conditionOne.await();
        }
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();
        twoDone = true;
        conditionTwo.signal();
        lock.unlock();
    }

    public void third(Runnable printThird) throws InterruptedException {
        lock.lock();
        while (!twoDone) {
            conditionTwo.await();
        }
        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
        lock.unlock();
    }
}

// easier solution with countdownlatch, doing very similar logic. it is a wrapper on top of wait and notify.
class Foo {

    private CountDownLatch latch1;
    private CountDownLatch latch2;

    public Foo() {
        this.latch1 = new CountDownLatch(1);
        this.latch2 = new CountDownLatch(1);
    }

    public void first(Runnable printFirst) throws InterruptedException {
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
        latch1.countDown();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        latch1.await();
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();
        latch2.countDown();
    }

    public void third(Runnable printThird) throws InterruptedException {
        latch2.await();
        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
    }
}
