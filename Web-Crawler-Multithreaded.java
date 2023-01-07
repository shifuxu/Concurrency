/**
 * // This is the HtmlParser's API interface.
 * // You should not implement it, or speculate about its implementation
 * interface HtmlParser {
 *     public List<String> getUrls(String url) {}
 * }
 */
class Solution {
    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        // find hostname
        int index = startUrl.indexOf('/', 7);
        String hostname = (index == -1) ? startUrl : startUrl.substring(0, index);


        ExecutorService executorService = Executors.newFixedThreadPool(4, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });

        Set<String> res = new HashSet<>();
        BlockingQueue<String> queue = new LinkedBlockingDeque<>();
        List<Future> tasks = new LinkedList<>();
        queue.offer(startUrl);

        while (true) {
            String url = queue.poll();
            if (url != null) {
                if (url.startsWith(hostname) && !res.contains(url)) {
                    res.add(url);
                    tasks.add(executorService.submit(() -> {
                        queue.addAll(htmlParser.getUrls(url));
                    }));
                }
            } else {
                if (!tasks.isEmpty()) {
                    Future future = tasks.get(0);
                    tasks.remove(0);
                    try {
                        future.get(); // blocking call, wait for result
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    // all the task has completed, exit
                    break;
                }
            }
        }
        return new ArrayList<>(res);
    }
}
