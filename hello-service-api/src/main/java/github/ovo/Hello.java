package github.ovo;

import lombok.*;

import java.io.Serializable;

/**
 * @author QAQ
 * @date 2021/9/17
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Hello implements Serializable {
    private String message;
    private String description;

}
