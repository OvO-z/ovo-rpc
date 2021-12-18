package github.ovo.registry.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import github.ovo.registry.ServiceRegistry;
import github.ovo.registry.nacos.util.NacosNamingServiceUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author QAQ
 * @date 2021/9/20
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {


    @Override
    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        NacosNamingServiceUtils.registerService(serviceName, inetSocketAddress);
    }
}
