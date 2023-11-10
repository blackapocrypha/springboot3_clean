package ${package.Entity}.enums;


/**
 * ${table.comment!} 枚举类
 *
 * @author ${author}
 * @since ${date}
*/
public enum  ${entity}Enum {
    /**
     * 用于分辨
     */
    STATUS_PROCESS("已处理", 0),
    STATUS_NO_PROCESS("未处理", 1),
    STATUS_OTHER("其他", -1),
    ;

    /**
     * 说明
     */
    private final String meaning;

    /**
     * 代码
     */
    private final Integer code;

    public String getMeaning() {
        return meaning;
    }

    public Integer getCode() {
        return code;
    }

    ${entity}Enum(String meaning, Integer code) {
        this.meaning = meaning;
        this.code = code;
    }

    /**
    * 是否是合法状态
    * @param code 状态码
    * @return
    */
    public static ${entity}Enum getEnumByCode(Integer code) {
        if (code==null) {
            return STATUS_OTHER;
        }

        for (${entity}Enum item : ${entity}Enum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return STATUS_OTHER;
    }

}