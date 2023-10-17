package wakoo.fun.service.VideosService.impl;

import org.springframework.stereotype.Service;
import wakoo.fun.dto.VideosDto;
import wakoo.fun.vo.SubclassVo;
import wakoo.fun.dto.CategoryDto;
import wakoo.fun.mapper.VideosMapper;
import wakoo.fun.pojo.Videos;
import wakoo.fun.service.VideosService.VideosService;
import wakoo.fun.vo.VideosVo;

import javax.annotation.Resource;
import java.util.List;
@Service
public class VideosServiceImpl implements VideosService {
    @Resource
    private VideosMapper videosMapper;

    @Override
    public List<Videos> getAllVideos(String keyword) {
        return videosMapper.getAllVideos(keyword);
    }

    @Override
    public List<CategoryDto> getfTypeDto() {
        return videosMapper.getfTypeDto();
    }

    @Override
    public List<SubclassVo> getAllSubclass(Integer fid) {
        return videosMapper.getAllSubclass(fid);
    }

    @Override
    public Boolean addVideo(VideosVo videosVo) {
        return videosMapper.addVideo(videosVo);
    }

    @Override
    public Integer queryParentLevel(Integer id, String typeName, Integer typeAge) {
        return videosMapper.queryParentLevel(id, typeName, typeAge);
    }

    @Override
    public VideosDto exampleModifyTheCommandOutput(Integer specifiedEcho) {
        return videosMapper.exampleModifyTheCommandOutput(specifiedEcho);
    }

    @Override
    public Boolean modifyTheCourseVideo(VideosVo videosVo) {
        return videosMapper.modifyTheCourseVideo(videosVo);
    }

    @Override
    public Boolean deleteVideo(Integer[] ids) {
        return videosMapper.deleteVideo(ids);
    }

    @Override
    public List<Videos> queryVideosBasedOnMultipleCriteria(String typeName,String classTypeName,String title,String videoLength,Integer which) {
        return videosMapper.queryVideosBasedOnMultipleCriteria(typeName,classTypeName ,title,videoLength,which );
    }
}
