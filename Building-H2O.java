class H2O {
    private Semaphore hSem;
    private Semaphore oSem;
    private AtomicInteger hCount;

    public H2O() {
        this.hSem = new Semaphore(2);
        this.oSem = new Semaphore(1);
        this.hCount = new AtomicInteger(0);
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
		hSem.acquire();
        hCount.incrementAndGet();
        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
        releaseHydrogen.run();
        if (hCount.get() == 2) {
            oSem.release();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oSem.acquire();
        // releaseOxygen.run() outputs "O". Do not change or remove this line.
		releaseOxygen.run();
        if (hCount.get() == 2) {
            hCount.decrementAndGet();
            hCount.decrementAndGet();
            hSem.release(2);
        }
    }
}
