package github.ovo.remoting.transport;

import github.ovo.extension.SPI;
import github.ovo.remoting.dto.RpcRequest;

/**
 *
 * 传输 RpcRequest。
 * 
 * @author QAQ
 * @date 2021/9/20
 */

@SPI
public interface ClientTransport {
    /**
     * 发送消息到服务端
     *
     * @param rpcRequest 消息体
     * @return 服务端返回的数据
     */
    Object sendRpRequest(RpcRequest rpcRequest);
}
