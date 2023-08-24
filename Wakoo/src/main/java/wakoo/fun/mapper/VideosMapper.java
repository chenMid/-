package wakoo.fun.mapper;

import org.apache.ibatis.annotations.Param;
import wakoo.fun.dto.VideosDto;
import wakoo.fun.vo.SubclassVo;
import wakoo.fun.dto.CategoryDto;
import wakoo.fun.pojo.Videos;
import wakoo.fun.vo.VideosVo;

import java.util.List;

public interface VideosMapper {
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
     * @param id - ID参数，表示某个特定对象的标识符。
     * @param typeName - typeName参数，表示对象的类型名称。
     * @param typeAge - typeAge参数，表示对象的年龄。
     * @return Integer类型的结果，表示查询到的父级层次。
     */
    Integer queryParentLevel(@Param("id") Integer id, @Param("typeName") String typeName, @Param("typeAge") Integer typeAge);

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
    Boolean modifyTheCourseVideo(@Param("videosVo") VideosVo videosVo);

    /**
     *  删除课程视频
     * @param ids 删除视频Id
     * @return 成功返回true，否则返回false
     */
    Boolean deleteVideo(Integer[] ids);
}
