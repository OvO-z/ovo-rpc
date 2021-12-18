package github.ovo.registry;

import github.ovo.extension.SPI;

import java.net.InetSocketAddress;

/**
 *
 * 服务发现接口
 * @author QAQ
 * @date 2021/9/20
 */

@SPI
public interface ServiceDiscovery {
    /**
     * 查找服务
     *
     * @param serviceName 服务名称
     * @return 提供服务的地址
     */
    InetSocketAddress lookupService(String serviceName);
}
