package wakoo.fun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressOption {
    private String value;
    private String label;
    private List<AddressOption> children;

    public AddressOption(String value, String label) {
        this.value = value;
        this.label = label;
        this.children = new ArrayList<>();
    }



    public void addChild(AddressOption child) {
        children.add(child);
    }
}
