package github.ovo.registry;

import github.ovo.extension.SPI;

import java.net.InetSocketAddress;

/**
 *
 * 服务注册中心接口
 *
 * @author QAQ
 * @date 2021/9/19
 */

@SPI
public interface ServiceRegistry {
    /**
     * 注册服务
     *
     * @param serviceName       服务名称
     * @param inetSocketAddress 提供服务的地址
     */
    void registerService(String serviceName, InetSocketAddress inetSocketAddress);

}
