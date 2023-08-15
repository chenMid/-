package wakoo.fun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wakoo.fun.dto.AdminAdministraltion;

import java.util.List;

/**
 * @author HASEE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Option {
//    private String value;
//    private String label;
//
//    @Override
//    public String toString() {
//        return "Option{" +
//                "value='" + value + '\'' +
//                ", label='" + label + '\'' +
//                '}';
//    }
    private String label;
    private List<AdminAdministraltion> options;

}
