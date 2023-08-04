package wakoo.fun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInformationDto {
    private String fileName;
    private String fileUrl;
    private String fileSize;
    private String mimeType;
    private String uploadTime;
    private String imageWidth;
    private String imageHeight;

}
