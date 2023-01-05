class FooBar {
    private int n;
    private Lock lock = new ReentrantLock();
    private Condition conditionFoo = lock.newCondition();
    private Condition conditionBar = lock.newCondition();
    private boolean changeToFoo = true;
    private boolean changeToBar = false;

    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            lock.lock();
            while (!changeToFoo) {
                conditionFoo.await();
            }
        	// printFoo.run() outputs "foo". Do not change or remove this line.
        	printFoo.run();
            changeToFoo = false;
            changeToBar = true;
            conditionBar.signal();
            lock.unlock();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        
        for (int i = 0; i < n; i++) {
            lock.lock();
            while (!changeToBar) {
                conditionBar.await();
            }
            // printBar.run() outputs "bar". Do not change or remove this line.
        	printBar.run();
            changeToBar = false;
            changeToFoo = true;
            conditionFoo.signal();
            lock.unlock();
        }
    }
}
