package wakoo.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideosDto {
    private Integer id;
    private String typeName;
    private String subclassName;
    private String title;
    private Integer which;
    private String videoImageUrl;
    private String videoUrl;
    private String videoIntroduction;
}
