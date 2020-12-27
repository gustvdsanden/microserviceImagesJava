package com.example.demo;

import com.example.demo.model.Image;
import com.example.demo.repository.ImageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageRepository imageRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenImage_whenGetImages_thenReturnJsonImages() throws Exception {

        Image image1 = new Image(
                "app/img1",
                "com1@hotmail.com",
                "test discription"
        );

        Image image2 = new Image(
                "app/img2",
                "com2@hotmail.com",
                "test discription"
        );


        List<Image> imageList = new ArrayList<>();
        imageList.add(image1);
        imageList.add(image2);

        given(imageRepository.findAll()).willReturn(imageList);

        mockMvc.perform(get("/images"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].source",is("app/img1")))
                .andExpect(jsonPath("$[0].description",is("test discription")))
                .andExpect(jsonPath("$[0].userEmail",is("com1@hotmail.com")))
                .andExpect(jsonPath("$[1].source",is("app/img2")))
                .andExpect(jsonPath("$[1].description",is("test discription")))
                .andExpect(jsonPath("$[1].userEmail",is("com2@hotmail.com")));
    }

     @Test
    public void givenImage_whenGetImageByKey_thenReturnJsonImage() throws Exception{
         Image image1 = new Image(
                 "app/img1",
                 "com1@hotmail.com",
                 "test discription"
         );

         given(imageRepository.findImagesByKey(image1.getKey())).willReturn(image1);

        mockMvc.perform(get("/images/{key}", image1.getKey()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source", is("app/img1")))
                .andExpect(jsonPath("$.description", is("test discription")))
                .andExpect(jsonPath("$.userEmail", is("com1@hotmail.com")));
    }

   @Test
    public void givenImage_whenPostImage_thenReturnJsonImage() throws Exception {
       Image image = new Image(
               "app/img1",
               "com1@hotmail.com",
               "test discription"
       );

        mockMvc.perform(post("/images")
                .content(mapper.writeValueAsString(image))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source", is("app/img1")))
                .andExpect(jsonPath("$.userEmail", is("com1@hotmail.com")))
                .andExpect(jsonPath("$.description", is("test discription")));

    }

    @Test
    public void giveImage_whenPutImage_thenReturnJsonImage() throws Exception {
        Image image1 = new Image(
                "app/img1",
                "com1@hotmail.com",
                "test discription"
        );

        given(imageRepository.findImagesByKey(image1.getKey())).willReturn(image1);

        image1.setSource("app/img1");
        image1.setDescription("this is a put Title");
        image1.setUserEmail("put@hotmail.com");

        mockMvc.perform(put("/images")
                .content(mapper.writeValueAsString(image1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source", is("app/img1")))
                .andExpect(jsonPath("$.userEmail", is("put@hotmail.com")))
                .andExpect(jsonPath("$.description", is("this is a put Title")));
    }

   @Test
    public void givenImage_whenDeleteImage_thenStatusOk() throws Exception {
       Image image1 = new Image(
               "app/img1",
               "com1@hotmail.com",
               "test discription"
       );

       given(imageRepository.findImagesByKey(image1.getKey())).willReturn(image1);

        mockMvc.perform(delete("/images/{key}", image1.getKey())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

     @Test
    public void givenNoImage_whenDeleteImage_thenStatusNotFound() throws Exception {
         given(imageRepository.findImagesByKey("0bb58513b1b57a7c7f8963f001f9896705b1ab2a22e320e4eb0ea2a985084fa")).willReturn(null);
        mockMvc.perform(delete("/images/{key}", "0bb58513b1b57a7c7f8963f001f9896705b1ab2a22e320e4eb0ea2a985084fa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
