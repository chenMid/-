package wakoo.fun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subclass {
    private Integer id;
    private Integer zId;
    private String name;
    private Integer typeAge;
    private String inageImage;
    private String ageImage;
    private String material;
    private Integer sort;
    private String createTime;

    public Subclass(Integer id, String typeName, String name, Integer typeAge, String inageImage, String ageImage, String material) {
        this.id = id;
        this.name = name;
        this.typeAge = typeAge;
        this.inageImage = inageImage;
        this.ageImage = ageImage;
        this.material = material;
    }

    public Subclass(Integer id, String name, Integer typeAge, String inageImage, String ageImage, String material, Integer sort) {
        this.id = id;
        this.name = name;
        this.typeAge = typeAge;
        this.inageImage = inageImage;
        this.ageImage = ageImage;
        this.material = material;
        this.sort = sort;
    }
}
