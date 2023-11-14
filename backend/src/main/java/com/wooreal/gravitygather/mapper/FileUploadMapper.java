package com.wooreal.gravitygather.mapper;

import com.wooreal.gravitygather.dto.file.FileVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileUploadMapper {

    FileVO getFileInfoBySeq(Integer seq);

    int singleFileUpload(FileVO file);
}
