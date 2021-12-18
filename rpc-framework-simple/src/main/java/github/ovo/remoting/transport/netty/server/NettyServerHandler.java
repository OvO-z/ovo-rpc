package github.ovo.remoting.transport.netty.server;

import github.ovo.enums.RpcMessageType;
import github.ovo.enums.RpcResponseCode;
import github.ovo.factory.SingletonFactory;
import github.ovo.handler.RpcRequestHandler;
import github.ovo.remoting.dto.RpcRequest;
import github.ovo.remoting.dto.RpcResponse;
import github.ovo.utils.concurrent.threadpool.CustomThreadPoolConfig;
import github.ovo.utils.concurrent.threadpool.ThreadPoolFactoryUtils;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * Netty服务端 handler
 * @author QAQ
 * @date 2021/9/18
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static RpcRequestHandler rpcRequestHandler;
    private static final String THREAD_NAME_PREFIX = "netty-server-handler-rpc-pool";
    private static final ExecutorService threadPool;

    static {
        /**
         * 单例工厂方法解决其他对象创建该bean
         * TODO 后期采用 Spring 进行依赖注入
         */
        rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
        CustomThreadPoolConfig customThreadPoolConfig = new CustomThreadPoolConfig();
        customThreadPoolConfig.setCorePoolSize(6);
        threadPool = ThreadPoolFactoryUtils.createCustomThreadPoolIfAbsent(THREAD_NAME_PREFIX, customThreadPoolConfig);
        // 打印线程池状态
//        ThreadPoolFactoryUtils.printThreadPoolStatus((ThreadPoolExecutor) threadPool);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("server receive msg: [{}]", msg);
        RpcRequest rpcRequest = (RpcRequest) msg;
        if (rpcRequest.getRpcMessageType() == RpcMessageType.HEART_BEAT) {
            log.info("receive heat beat msg from client");
            return;
        }
        threadPool.execute(() -> {
            log.info(String.format("server handle message from client by thread: %s", Thread.currentThread().getName()));
            try {


                //执行目标方法（客户端需要执行的方法）并且返回方法结果
                Object result = rpcRequestHandler.handle(rpcRequest);
                log.info("server get result: [{}]", result.toString());
                if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                    //返回方法执行结果给客户端
                    ctx.writeAndFlush(RpcResponse.success(result, rpcRequest.getRequestId()));
                } else {
                    RpcResponse<Object> rpcResponse = RpcResponse.fail(RpcResponseCode.FAIL);
                    ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                    log.error("not writable now, message dropped");
                }
            } finally {
                //确保 ByteBuf 被释放，不然可能会有内存泄露问题
                ReferenceCountUtil.release(msg);
            }
        });
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("idle check happen, so close the connection");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}
