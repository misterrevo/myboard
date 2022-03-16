package com.revo.myboard.image;

import com.revo.myboard.image.dto.ImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

/*
 * Created By Revo
 */

@Controller
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageControler {

    private static final String LOCATION = "/images";

    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<ImageDTO> saveImage(@RequestParam MultipartFile multipartFile){
        var imageDTO = imageService.saveImage(multipartFile);
        return ResponseEntity.created(URI.create(LOCATION)).body(imageDTO);
    }

    @GetMapping("/{name}")
    public @ResponseBody Resource getImage(@PathVariable String name){
        return imageService.getImageResource(name);
    }

}
