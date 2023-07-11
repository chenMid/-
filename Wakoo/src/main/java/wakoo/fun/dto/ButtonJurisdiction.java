package wakoo.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ButtonJurisdiction {
    private Integer id;
    private Integer menuId;
    private String name;

    @Override
    public String toString() {
        return "ButtonJurisdiction{" +
                "id=" + id +
                ", menuId=" + menuId +
                ", name='" + name + '\'' +
                '}';
    }
}
