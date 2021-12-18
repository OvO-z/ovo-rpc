package github.ovo.registry.nacos;

import github.ovo.registry.ServiceDiscovery;
import github.ovo.registry.nacos.util.NacosNamingServiceUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * 基于 nacos 实现服务发现
 *
 * @author QAQ
 * @date 2021/9/20
 */

@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {


    @Override
    public InetSocketAddress lookupService(String serviceName) {
        return NacosNamingServiceUtils.lookupService(serviceName);
    }
}
