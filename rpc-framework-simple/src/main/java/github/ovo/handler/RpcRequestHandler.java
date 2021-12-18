package github.ovo.handler;

import github.ovo.entity.RpcServiceProperties;
import github.ovo.factory.SingletonFactory;
import github.ovo.remoting.dto.RpcRequest;
import github.ovo.remoting.dto.RpcResponse;
import github.ovo.enums.RpcResponseCode;
import github.ovo.exception.RpcException;
import github.ovo.provider.ServiceProvider;
import github.ovo.provider.ServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author QAQ
 * @date 2021/9/19
 */

@Slf4j
public class RpcRequestHandler {

    private final ServiceProvider SERVICE_PROVIDER;

    public RpcRequestHandler() {
        SERVICE_PROVIDER = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    /**
     * 处理 rpcRequest 然后返回方法执行结果
     */
    public Object handle(RpcRequest rpcRequest) {
        //通过注册中心获取到目标类（客户端需要调用类）
        Object service = SERVICE_PROVIDER.getService(rpcRequest.toRpcProperties());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * 根据 rpcRequest 和 service 对象特定的方法并返回结果
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service)  {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            if (null == method) {
                return RpcResponse.fail(RpcResponseCode.FAIL);
            }
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service:[{}] successful invoke method:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
