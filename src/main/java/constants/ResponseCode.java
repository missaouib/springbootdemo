package constants;

public enum ResponseCode {
    SUCCESS(0, ""),
    //参数校验相关
    PARAM_ERROR(400, "参数不合法"),
    PARAM_REPEAT(4015, "参数重复"),

    //服务器内部错误相关
    SERVER_ERROR(500, "服务器内部错误"),
    ;


    ResponseCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    final private Integer code;
    final private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
