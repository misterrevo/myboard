package com.revo.myboard.image;

import com.revo.myboard.exception.ImageNotExistsException;
import com.revo.myboard.exception.ImageSavingException;
import com.revo.myboard.image.dto.ImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

/*
 * Created By Revo
 */

@Service
@RequiredArgsConstructor
public class ImageService {

    private static final String RESOURCES_PATH = "src/main/resources/";

    private final ImageRepository imageRepository;
    
    ImageDTO saveImage(MultipartFile multipartFile){
        var image = buildImage(multipartFile);
        var savedImage = imageRepository.save(image);
        saveToResources(multipartFile, savedImage);
        return map(savedImage);
    }

    private void saveToResources(MultipartFile multipartFile, Image savedImage) {
        File file = new File(RESOURCES_PATH+savedImage.getName()+"."+savedImage.getType());
        save(file, multipartFile, savedImage);
    }

    private void save(File file, MultipartFile multipartFile, Image image) {
        try {
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(multipartFile.getBytes());
        } catch (Exception e) {
            throw new ImageSavingException(image.getName());
        }
    }

    private ImageDTO map(Image savedImage) {
        return ImageMapper.mapFromImage(savedImage);
    }

    private Image buildImage(MultipartFile multipartFile){
        return Image.builder()
                .name(generateName())
                .type(multipartFile.getContentType())
                .build();
    }

    private String generateName() {
        return UUID.randomUUID().toString();
    }

    Resource getImageResource(String name){
        var path = getPath(name);
        return new ClassPathResource(path);
    }

    private String getPath(String name) {
        var image = getImage(name);
        return RESOURCES_PATH+image.getName()+"."+image.getType();
    }

    private Image getImage(String name) {
        return imageRepository.findByName(name).orElseThrow(() -> new ImageNotExistsException(name));
    }

}
