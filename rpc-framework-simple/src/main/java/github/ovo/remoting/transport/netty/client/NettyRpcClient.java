package github.ovo.remoting.transport.netty.client;

import github.ovo.extension.ExtensionLoader;
import github.ovo.remoting.dto.RpcRequest;
import github.ovo.remoting.dto.RpcResponse;
import github.ovo.remoting.transport.netty.codec.kryo.NettyKryoDecoder;
import github.ovo.remoting.transport.netty.codec.kryo.NettyKryoEncoder;
import github.ovo.serialize.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 用于初始化 和 关闭 Bootstrap 对象
 * @author QAQ
 * @date 2021/9/19
 */

@Slf4j
public class NettyRpcClient {
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    // 初始化相关资源比如 EventLoopGroup、Bootstrap
    static {
        eventLoopGroup = new NioEventLoopGroup();
        Serializer kryoSerializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension("kryo");
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
//                //是否开启 TCP 底层心跳机制
//                .option(ChannelOption.SO_KEEPALIVE, true)
//                //TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
//                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        // 要将IdleStateHandler 放最上面
                        ch.pipeline().addLast(new IdleStateHandler(0,5,0, TimeUnit.SECONDS));
                        ch.pipeline().addLast(new NettyKryoEncoder(kryoSerializer, RpcRequest.class));
                        ch.pipeline().addLast(new NettyKryoDecoder(kryoSerializer, RpcResponse.class));
                        ch.pipeline().addLast(new NettyClientHandler());

                    }
                });
    }

    @SneakyThrows
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
           if (future.isSuccess()) {
               log.info("客户端连接成功！！！");
               completableFuture.complete(future.channel());
           } else {
               throw new IllegalStateException();
           }
        });
        return completableFuture.get();
    }

    public static void close() {
        log.info("call close method");
        eventLoopGroup.shutdownGracefully();
    }
}
