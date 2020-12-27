package com.example.demo.controller;

import com.example.demo.model.Image;

import com.example.demo.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.annotation.PostConstruct;
import java.util.List;


@RestController
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;


    @PostConstruct
    public void fillDB(){
        if(imageRepository.count()==0){
            imageRepository.save(new Image("/img3/spacemaker","gustvdsanden@gmail.com","this image is beautifull"));
            imageRepository.save(new Image("/img4/spacemaker","thomasmiep@test.com","this image is pretty nice"));
        }
    }

    @GetMapping("/images")
    public List<Image> getAllUsers(){

        return imageRepository.findAll();
    }
    @GetMapping("/test")
    public String test(){

        return "test";
    }

    @GetMapping("/images/{key}")
    public Image getImages(@PathVariable String key)    {
        return imageRepository.findImagesByKey(key);
    }

    @PostMapping("/images")
    Image addImage(@RequestBody Image newImage)
    {
        imageRepository.save(newImage);
        return newImage;
    }
    @PutMapping("/images")
    public Image updateImage(@RequestBody Image updateImage)
    {
        Image image = imageRepository.findImagesByKey(updateImage.getKey());
        if(image != null){
            image.setSource(updateImage.getSource());
            image.setDescription(updateImage.getDescription());
            image.setUserEmail(updateImage.getUserEmail());
            imageRepository.save(image);
            return image;
        }else{
            return null;
        }

    }

    @DeleteMapping("/images/{key}")
    public ResponseEntity<Object> deleteImage(@PathVariable String key)
    {
        Image image = imageRepository.findImagesByKey(key);
        if(image != null){
            imageRepository.delete(image);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
