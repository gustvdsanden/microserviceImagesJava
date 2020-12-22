package com.example.demo.controller;

import com.example.demo.model.Image;

import com.example.demo.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
