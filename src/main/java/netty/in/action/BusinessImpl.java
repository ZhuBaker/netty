package netty.in.action;

/**
 * Created with IntelliJ IDEA.
 * Description: 业务接口实现类
 * User: zhubo
 * Date: 2018-02-02
 * Time: 14:49
 */
public class BusinessImpl implements BusinessInterface {

    @Override
    public void doSomeThing() {
        System.out.println("====业务实现类====");
    }
}
