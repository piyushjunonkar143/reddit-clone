package com.reddit.service;

import com.reddit.entity.Media;
import com.reddit.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    @Autowired
    FileRepository fileRepository;
    public List<Media> uploadImage(String path, List<MultipartFile> images) {
        List<Media> mediaList = new ArrayList<>();
        for(MultipartFile image: images) {
            Media media = new Media();
            String name = image.getOriginalFilename();
            if(name!=null && name.contains(".")) {
                String randomId = UUID.randomUUID().toString();
                String fileName = randomId.concat(name.substring(name.lastIndexOf('.')));
                String filePath = path + File.separator + fileName;
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdir();
                }
                if (image.getContentType().equals("video/mp4")) {
                    media.setIsVideo(true);
                } else {
                    media.setIsVideo(false);
                }
                media.setPathUrl(filePath);
                mediaList.add(fileRepository.save(media));
            }
        }
        return mediaList;
    }
}