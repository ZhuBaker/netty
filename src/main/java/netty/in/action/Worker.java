package netty.in.action;

/**
 * Created with IntelliJ IDEA.
 * Description: 业务驱动类
 * User: zhubo
 * Date: 2018-01-31
 * Time: 10:03
 */
public class Worker {

    public void doWork() {
        Fetcher fetcher = new FetcherImpl(new BusinessImpl());
        fetcher.fetchData(new FetcherCallback() {
            @Override
            public void onData(BusinessInterface data) throws Exception {
                System.out.println("Data received: " + data);
            }

            @Override
            public void onError(Throwable cause) {
                System.out.println("An error accour: " + cause.getMessage());
            }
        });
    }

    public static void main(String[] args) {
        Worker w = new Worker();
        w.doWork();
    }
}
