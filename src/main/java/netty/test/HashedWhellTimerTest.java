package netty.test;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

/**
 * Created by zhubo on 2018/3/28.
 */
public class HashedWhellTimerTest {
    static Timer timer = new HashedWheelTimer(50L,TimeUnit.MILLISECONDS,512);

    public static void main(String[] args) {

       TimerTask task = new TimerTask() {
           @Override
           public void run(Timeout timeout) throws Exception {
               System.out.println("---run service-----");
               //任务执行完成后再把自己添加到任务solt上
               addTask(this);
           }
       };
        addTask(task);
    }

    public static void addTask(TimerTask task){
        //根据时长把task任务放到响应的solt上
        timer.newTimeout(task,2,TimeUnit.SECONDS);
    }
}
