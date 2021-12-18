package github.ovo.provider;

import com.google.common.collect.Maps;
import github.ovo.entity.RpcServiceProperties;
import github.ovo.enums.RpcErrorMessage;
import github.ovo.exception.RpcException;
import github.ovo.extension.ExtensionLoader;
import github.ovo.registry.ServiceRegistry;
import github.ovo.remoting.transport.netty.server.NettyRpcServer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author QAQ
 * @date 2021/9/20
 */

@Slf4j
public class ServiceProviderImpl implements ServiceProvider{


    /**
     * 接口名和服务的对应关系
     * note:处理一个接口被两个实现类实现的情况如何处理？
     * key:service/interface name
     * value:service
     */
    private static final Map<String, Object> SERVICE_MAP = Maps.newConcurrentMap();
    private static final Set<String> REGISTERED_SERVICE = ConcurrentHashMap.newKeySet();

    private final ServiceRegistry serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension("nacos");

    /**
     * note:可以修改为扫描注解注册
     * 将这个对象所有实现的接口都注册进去
     */
    @Override
    public void addServiceProvider(Object service, Class<?> serviceClass, RpcServiceProperties rpcServiceProperties) {
        String rpcServiceName = rpcServiceProperties.toRpcServiceName();
        if (REGISTERED_SERVICE.contains(rpcServiceName)) {
            return;
        }
        REGISTERED_SERVICE.add(rpcServiceName);
        SERVICE_MAP.put(rpcServiceName, service);
        log.info("Add service: [{}] and interfaces: [{}]", rpcServiceName, service.getClass().getInterfaces());
    }

    @Override
    public Object getService(RpcServiceProperties rpcServiceProperties) {
        Object service = SERVICE_MAP.get(rpcServiceProperties.toRpcServiceName());
        if (null == service) {
            throw new RpcException(RpcErrorMessage.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(Object service) {
        this.publishService(service, RpcServiceProperties.builder().group("").version("").build());
    }

    @Override
    public void publishService(Object service, RpcServiceProperties rpcServiceProperties) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            Class<?> serviceRelatedInterface = service.getClass().getInterfaces()[0];
            String serviceName = serviceRelatedInterface.getCanonicalName();
            rpcServiceProperties.setServiceName(serviceName);
            this.addServiceProvider(service, serviceRelatedInterface, rpcServiceProperties);
            serviceRegistry.registerService(rpcServiceProperties.toRpcServiceName(), new InetSocketAddress(host, NettyRpcServer.PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
