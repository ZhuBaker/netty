package netty.in.action;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-01-31
 * Time: 10:03
 */
public interface Fetcher {

    void fetchData(FetcherCallback callback);

}