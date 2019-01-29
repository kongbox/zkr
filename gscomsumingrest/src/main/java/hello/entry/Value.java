package hello.entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Title:
 * Description:
 *
 * @author kdc
 * @version 1.0
 * @date 2019/1/28.16:53 首次创建
 * @date 2019/1/28.16:53 最后修改
 * @copyright 中科软科技股份有限公司
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Value {

    private Long id;
    private String quote;

    public Value() {
    }

    public Long getId() {
        return this.id;
    }

    public String getQuote() {
        return this.quote;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return "Value{" +
                "id=" + id +
                ", quote='" + quote + '\'' +
                '}';
    }
}