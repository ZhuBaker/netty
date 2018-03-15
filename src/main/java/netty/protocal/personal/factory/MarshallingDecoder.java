package netty.protocal.personal.factory;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhubo
 * Date: 2018-03-15
 * Time: 11:24
 */
public class MarshallingDecoder {

    private final Unmarshaller unmarshaller;

    /**
     * Creates a new decoder whose maximum object size is {@code 1048576} bytes.
     * If the size of the received object is greater than {@code 1048576} bytes,
     * a {@link IOException} will be raised.
     *
     * @throws IOException
     */
    public MarshallingDecoder() throws IOException {
        unmarshaller = MarshallingCodecFactory.buildUnMarshalling();
    }

    public Object decode(ByteBuf in) throws Exception {
        //1. 读取第一个4bytes，里面放置的是object对象的byte长度
        int objectSize = in.readInt();
        ByteBuf buf = in.slice(in.readerIndex(), objectSize);
        //2 . 使用bytebuf的代理类
        ByteInput input = new ChannelBufferByteInput(buf);
        try {
            //3. 开始解码
            unmarshaller.start(input);
            Object obj = unmarshaller.readObject();
            unmarshaller.finish();
            //4. 读完之后设置读取的位置
            in.readerIndex(in.readerIndex() + objectSize);
            return obj;
        } finally {
            unmarshaller.close();
        }
    }



}
