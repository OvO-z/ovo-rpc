package github.ovo.registry.nacos.util;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author QAQ
 * @date 2021/9/20
 */
@Slf4j
public class NacosNamingServiceUtils {

    private static final String CONNECT_STRING = "127.0.0.1:8848";
    private static final NamingService namingService = createNamingService();

    public static NamingService createNamingService() {
        NamingService namingService;
        try {
            namingService = NacosFactory.createNamingService(CONNECT_STRING);
        } catch (NacosException e) {
            log.error(e.getErrMsg(), e);
            throw new IllegalStateException(e);
        }
        return namingService;
    }

    public static void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            namingService.registerInstance(serviceName, inetSocketAddress.getAddress().getHostAddress(), inetSocketAddress.getPort());
        } catch (NacosException e) {
            log.error("occur registry exception:", e);
        }
    }

    public static InetSocketAddress lookupService(String serviceName) {
        try {
            Instance instance = namingService.selectOneHealthyInstance(serviceName);
            log.info("成功找到服务地址:[{}]:[{}]", instance.getIp(),instance.getPort());
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("occur look up exception:", e);
        }
        return null;
    }
}
