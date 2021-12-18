package github.ovo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author QAQ
 * @date 2021/9/18
 */
@AllArgsConstructor
@Getter
@ToString
public enum RpcResponseCode {
    SUCCESS(200,"调用方法成功"),
    FAIL(500,"调用方法失败");
    private final int code;

    private final String message;
}
