package github.ovo.remoting.transport.netty.client;

import github.ovo.extension.ExtensionLoader;
import github.ovo.factory.SingletonFactory;
import github.ovo.registry.ServiceDiscovery;
import github.ovo.remoting.dto.RpcRequest;
import github.ovo.remoting.dto.RpcResponse;
import github.ovo.remoting.transport.ClientTransport;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 *
 * 基于 Netty 传输 RpcRequest。
 *
 * @author QAQ
 * @date 2021/9/19
 */

@Slf4j
public class NettyClientTransport implements ClientTransport {

    private final UnprocessedRequests unprocessedRequests;
    private ServiceDiscovery serviceDiscovery;
    private final ChannelProvider channelProvider;

    public NettyClientTransport() {
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("nacos");
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    /**
     * 发送消息到服务端
     *
     * @param rpcRequest 消息体
     * @return 服务端返回的数据
     */
    @Override
    public CompletableFuture<RpcResponse> sendRpRequest(RpcRequest rpcRequest) {
        // 构建返回值
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();

        // 构造RPC服务参数
        String rpcServiceName = rpcRequest.toRpcProperties().toRpcServiceName();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcServiceName);
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel != null && channel.isActive()) {
            // 放入未处理的请求
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("client send message: [{}]", rpcRequest);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("Send failed:", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }
        return resultFuture;
    }
}
