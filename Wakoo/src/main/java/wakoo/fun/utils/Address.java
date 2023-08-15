package wakoo.fun.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址实体类
 * @author HASEE
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address  {

    @Id
    private String id;

    private String label;

    private Address parent;

    // Getters and Setters
}
