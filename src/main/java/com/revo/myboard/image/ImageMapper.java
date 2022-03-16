package com.revo.myboard.image;

import com.revo.myboard.image.dto.ImageDTO;

/*
 * Created By Revo
 */

public class ImageMapper {

    public static ImageDTO mapFromImage(Image image){
        return buildImage(image);
    }

    private static ImageDTO buildImage(Image image) {
        return ImageDTO.builder()
                .name(image.getName())
                .type(image.getType())
                .build();
    }

}
