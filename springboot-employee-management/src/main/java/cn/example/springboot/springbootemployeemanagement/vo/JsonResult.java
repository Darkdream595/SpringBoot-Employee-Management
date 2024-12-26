package cn.example.springboot.springbootemployeemanagement.vo;

public class JsonResult<T> {
    private int code;
    private String message;
    private T data;

    public JsonResult() {
    }

    public JsonResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(200, "success", data);
    }

    public static <T> JsonResult<T> success(String message, T data) {
        return new JsonResult<>(200, message, data);
    }

    public static <T> JsonResult<T> error(String message) {
        return new JsonResult<>(500, message, null);
    }

    public static <T> JsonResult<T> fail(String message) {
        return new JsonResult<>(500, message, null);
    }

    public static <T> JsonResult<T> fail(Integer code, String message) {
        return new JsonResult<>(code, message, null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}