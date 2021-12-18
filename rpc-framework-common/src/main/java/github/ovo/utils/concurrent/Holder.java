package github.ovo.utils.concurrent;

/**
 * @author QAQ
 * @date 2021/9/22
 */

public class Holder<T> {
    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
