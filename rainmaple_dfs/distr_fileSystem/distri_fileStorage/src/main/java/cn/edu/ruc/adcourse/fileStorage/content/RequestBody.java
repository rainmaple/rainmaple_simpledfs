package cn.edu.ruc.adcourse.fileStorage.content;

/**
 * 请求内容
 */
public class RequestBody<T> {
    private int code;               //要请求的code，用于区分不同的请求
    private T data;                 //请求时附带的数据

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public RequestBody(int code, T data) {
        this.code = code;
        this.data = data;
    }

    private RequestBody(RequestBody.Builder<T> builder) {
        code = builder.code;
        data = builder.data;
    }

    public static final class Builder<T> {
        private int code;
        private T data;

        public Builder() {
        }

        public Builder code(int val) {
            code = val;
            return this;
        }

        public Builder data(T val) {
            data = val;
            return this;
        }

        public RequestBody<T> build() {
            return new RequestBody<T>(this);
        }
    }
}
