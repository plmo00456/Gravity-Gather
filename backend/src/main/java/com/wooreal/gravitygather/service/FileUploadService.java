package com.wooreal.gravitygather.service;

import com.wooreal.gravitygather.dto.file.FileVO;
import com.wooreal.gravitygather.exception.BusinessLogicException;
import com.wooreal.gravitygather.mapper.FileUploadMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

    private final FileUploadMapper fileUploadMapper;

    public FileUploadService(FileUploadMapper fileUploadMapper){
        this.fileUploadMapper = fileUploadMapper;
    }

    @Value("${file.upload.dir}")
    private String uploadDir;

    public FileVO getFileInfoBySeq(Integer seq){
        FileVO uploadFile = fileUploadMapper.getFileInfoBySeq(seq);
        if(uploadFile == null || uploadFile.getSeq().equals(0)){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "파일 정보를 가져오는 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }
        return uploadFile;
    }

    public FileVO singleFileUpload(MultipartFile file, String ...addPath) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        String newFilename = UUID.randomUUID().toString() + extension;

        Path filePath = Paths.get(uploadDir, newFilename);
        if(addPath.length > 0)
            filePath = Paths.get(uploadDir + "/" + addPath[0], newFilename);

        File saveFile = new File(filePath.toString());
        file.transferTo(saveFile);

        FileVO uploadFile = new FileVO();
        uploadFile.setUpload_path(filePath.toString());
        uploadFile.setExtension(extension);
        uploadFile.setOriginal_name(originalFilename);
        uploadFile.setNew_name(newFilename);

        int upload = fileUploadMapper.singleFileUpload(uploadFile);
        if(upload == 0 || uploadFile.getSeq() == 0){
            throw new BusinessLogicException(HttpStatus.valueOf(500), "파일 업로드 중 오류가 발생했습니다. 관리자에게 문의해 주세요.");
        }

        return getFileInfoBySeq(uploadFile.getSeq());
    }
}