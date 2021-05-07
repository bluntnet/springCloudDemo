package demo.helper;

import lombok.Getter;
import lombok.Setter;

/**
 * Http Ajax 请求返回时用作返回的对象，FastJson 自动转换为 Json 字符串返回给前端。
 * 提供了多个变种的 ok() 和 fail() 方法简化创建 Result 对象。
 *
 * 虽然同一个请求在不同情况下返回的 Result 中的 data 类型可能不同，例如 Result<User> findUserByName(String name)，
 * 查询到用户时返回 Result 中 data 是 User 对象，查询不到用户时可返回 Result 中 data 是 String 对象，不过没关系，
 * 在我们的实现中允许这么做，好处是标志出了请求正确响应时返回的数据类型，因为这个才是我们最关心的。
 */
@Getter
@Setter
public final class Result<T> {
    private boolean success; // 成功时为 true，失败时为 false
    private String  message; // 成功或则失败时的描述信息
    private Object  data;    // 成功或则失败时的更多详细数据，一般失败时不需要
    private Integer code;    // 状态码，一般是当 success 为 true 或者 false 时不足够表达时才使用，平时忽略即可

    public Result(boolean success, String message) {
        this(success, message, null);
    }

    public Result(boolean success, String message, Object data) {
        this(success, message, data, null);
    }

    public Result(boolean success, String message, Object data, Integer code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
    }

    public static <T> Result<T> ok() {
        return new Result<>(true, "success");
    }

    public static <T> Result<T> ok(Object data) {
        return new Result<>(true, "success", data);
    }

    public static <T> Result<T> ok(String message, Object data) {
        return new Result<>(true, message, data);
    }

    public static <T> Result<T> ok(String message, Object data, Integer code) {
        return new Result<>(true, message, data, code);
    }

    public static <T> Result<T> okMessage(String message) {
        return new Result<>(true, message, null);
    }

    public static <T> Result<T> fail() {
        return new Result<>(false, "fail");
    }

    public static <T> Result<T> fail(Object data) {
        return new Result<>(false, "fail", data);
    }

    public static <T> Result<T> fail(String message, Object data) {
        return new Result<>(false, message, data);
    }

    public static <T> Result<T> fail(String message, Object data, Integer code) {
        return new Result<>(false, message, data, code);
    }

    public static <T> Result<T> failMessage(String message) {
        return new Result<>(false, message, null);
    }

    /**
     * 返回单个对象时根据对象是否为空返回成功或者失败的不同结果:
     *     A. data 不为 null 时执行 Result.ok(data)
     *     B. data 等于 null 时执行 Result.failMessage(error)
     */
    public static <T> Result<T> single(T data) {
        return Result.single(data, "");
    }

    public static <T> Result<T> single(T data, String error) {
        return (data != null) ? Result.ok(data) : Result.failMessage(error);
    }
}
