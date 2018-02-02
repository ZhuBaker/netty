package netty.in.action;

/**
 * Created with IntelliJ IDEA.
 * Description: 业务接口实现类
 * User: zhubo
 * Date: 2018-01-31
 * Time: 10:06
 */
public class FetcherImpl implements Fetcher {

    final BusinessInterface data;

    public FetcherImpl(BusinessInterface data) {
        this.data = data;
    }

    /**
     * 实现业务逻辑 执行完后回调数据
     * @param callback
     */
    @Override
    public void fetchData(FetcherCallback callback) {
        try{
            //执行业务代码
            data.doSomeThing();
            // 调用回调
            callback.onData(data);
        }catch (Exception e){
            callback.onError(e);
        }
    }
}