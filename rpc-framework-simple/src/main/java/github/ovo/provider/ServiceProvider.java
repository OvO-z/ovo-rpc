package github.ovo.provider;

import github.ovo.entity.RpcServiceProperties;

/**
 * 保存和提供服务实例对象。服务端使用。
 * @author QAQ
 * @date 2021/9/20
 */

public interface ServiceProvider {

    /**
     * 保存服务实例对象和服务实例对象实现的接口类的对应关系
     *
     * @param service      服务实例对象
     * @param serviceClass 服务实例对象实现的接口类
     */
    void addServiceProvider(Object service, Class<?> serviceClass, RpcServiceProperties rpcServiceProperties);

    /**
     * 获取服务实例对象
     *
     * @param serviceName 服务实例对象实现的接口类的类名
     * @return 服务实例对象
     */
    Object getService(RpcServiceProperties rpcServiceProperties);



    /**
     * @param service              service object
     * @param rpcServiceProperties service related attributes
     */
    void publishService(Object service, RpcServiceProperties rpcServiceProperties);

    /**
     * 发布服务
     *
     * @param service 服务实例对象
     */
    void publishService(Object service);
}
