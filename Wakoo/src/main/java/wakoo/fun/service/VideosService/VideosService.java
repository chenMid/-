package wakoo.fun.service.VideosService;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.dto.VideosDto;
import wakoo.fun.vo.SubclassVo;
import wakoo.fun.dto.CategoryDto;
import wakoo.fun.pojo.Videos;
import wakoo.fun.vo.VideosVo;

import java.util.List;

public interface VideosService {
    /**
     * 获取所有及模糊查询
     * @return
     */
    List<Videos> getAllVideos(String keyword);
    /**
     * 查询父类
     * @return
     */
    List<CategoryDto> getfTypeDto();

    /**
     * 获取父类下的所有子类
     * @return
     */
    List<SubclassVo> getAllSubclass(Integer fid);
    /**
     * 添加视频
     * @param videosVo
     * @return
     */
    Boolean addVideo(@Param("videosVo") VideosVo videosVo);

    /**
     * 查询父子级
     * @param id-父级id
     * @param typeName-子类名称
     * @param typeAge-子类年龄
     * @return 返回Integer类型有返回的就是id没有返回的就是null
     */
    Integer queryParentLevel(Integer id, String typeName,Integer typeAge);
    /**
     * 指定回显
     * @param specifiedEcho 需要回显的Id
     * @return 返回对象VideosVo
     */
    VideosDto exampleModifyTheCommandOutput(@Param("specifiedEcho") Integer specifiedEcho);
    /**
     * 修改课程视频
     * @param videosVo 修改课程实体类包含要修改的属性
     * @return 成功返回true~false
     */
    Boolean modifyTheCourseVideo(@Param("videosVo") VideosVo videosVo, @Param("parentClassID") Integer parentClassID);
}
