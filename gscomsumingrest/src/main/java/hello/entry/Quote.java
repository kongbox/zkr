package hello.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Title:
 * Description:
 *
 * @author kdc
 * @version 1.0
 * @date 2019/1/28.15:28 首次创建
 * @date 2019/1/28.15:28 最后修改
 * @copyright 中科软科技股份有限公司
 */
@JsonIgnoreProperties(ignoreUnknown =true)
public class Quote {

    private String type;
    private Value value;

    public Quote(){
    }


    @Override
    public String toString() {
        return "Quote{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
