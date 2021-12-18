package github.ovo.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import github.ovo.remoting.dto.RpcRequest;
import github.ovo.remoting.dto.RpcResponse;
import github.ovo.exception.SerializeException;
import github.ovo.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Kryo序列化类
 * @author QAQ
 * @date 2021/9/19
 */

@Slf4j
public class KryoSerializer implements Serializer {

    /**
     * 由于 Kryo 不是线程安全的。每个线程都应该有自己的 Kryo，Input 和 Output 实例。
     * 所以，使用 ThreadLocal 存放 Kryo 对象
     */
    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
       Kryo kryo = new Kryo();
       kryo.register(RpcRequest.class);
       kryo.register(RpcResponse.class);
       //默认值为true,是否关闭注册行为,关闭之后可能存在序列化问题，一般推荐设置为 true
       kryo.setReferences(true);
       //默认值为false,是否关闭循环引用，可以提高性能，但是一般不推荐设置为 true
       kryo.setRegistrationRequired(false);
       return kryo;
    });
    @Override
    public byte[] serialize(Object obj) {
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            log.info("Kryo 序列化完成");
            return output.toBytes();
        } catch (IOException e) {
            log.error("occur exception when serialize: ", e);
            throw new SerializeException("序列化失败");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            // byte->Object:从byte数组中反序列化出对对象
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            log.info("Kryo 反序列化完成");
            return clazz.cast(o);
        } catch (Exception e) {
            log.error("occur exception when deserialize:", e);
            throw new SerializeException("反序列化失败");
        }
    }
}
