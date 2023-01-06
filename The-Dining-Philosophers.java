class DiningPhilosophers {

    private ReentrantLock[] locks;
    private Map<Integer, List<ReentrantLock>> map;

    public DiningPhilosophers() {
        this.locks = new ReentrantLock[5];
        this.map = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            this.locks[i] = new ReentrantLock();
            this.map.put(i, new ArrayList<>());
        }
        for (int i = 0; i < 5; i++) {
            this.map.get(i).add(locks[i]);
            if (i == 4) {
                this.map.get(i).add(locks[0]);
            } else {
                this.map.get(i).add(locks[i + 1]);
            }
        }
    }

    // call the run() method of any runnable to execute its code
    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException {
        // needs to acquire two locks to start eating
        Boolean gotLock = tryLock(philosopher);
        while (!gotLock) {
            Thread.sleep(10);
            gotLock = tryLock(philosopher);
        }

        pickLeftFork.run();
        pickRightFork.run();
        eat.run();
        putLeftFork.run();
        putRightFork.run();

        releaseLock(philosopher);
    }

    private void releaseLock(int philosopher) {
        List<ReentrantLock> lst = map.get(philosopher);
        ReentrantLock rightLock = lst.get(0);
        ReentrantLock leftLock = lst.get(1);

        rightLock.unlock();
        leftLock.unlock();
    }

    private boolean tryLock(int philosopher) {
        List<ReentrantLock> lst = map.get(philosopher);
        ReentrantLock rightLock = lst.get(0);
        ReentrantLock leftLock = lst.get(1);

        if (rightLock.tryLock()) {
            if (leftLock.tryLock()) {
                return true;
            } else {
                rightLock.unlock(); // need to release previous to avoid deadlock
                return false;
            }
        } else {
            return false;
        }
    }
}
