package github.ovo.exception;


import github.ovo.enums.RpcErrorMessage;

/**
 * @author shuang.kou
 * @createTime 2020年05月12日 16:48:00
 */
public class RpcException extends RuntimeException {
    public RpcException(RpcErrorMessage rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcErrorMessage rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }
}
