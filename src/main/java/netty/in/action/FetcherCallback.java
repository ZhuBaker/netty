package netty.in.action;

/**
 * Created with IntelliJ IDEA.
 * Description: 声明回调接口
 * User: zhubo
 * Date: 2018-01-31
 * Time: 10:04
 */
public interface FetcherCallback {

    void onData(BusinessInterface data) throws Exception;

    void onError(Throwable cause);

}