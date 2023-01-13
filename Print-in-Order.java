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
