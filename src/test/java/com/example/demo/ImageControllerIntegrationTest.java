package com.example.demo;

import com.example.demo.model.Image;
import com.example.demo.repository.ImageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImageRepository imageRepository;


    private Image image1 = new Image(
            "app/img",
            "youri1@hotmail.com",
            "Dat is mooi."
    );

    private Image image2 = new Image(
            "app/img2",
            "youri2@hotmail.com",
            "Dat is mooi."

    );

    private Image image3 = new Image(
            "app/img3",
            "youri3@hotmail.com",
            "Dat is mooi."

    );

    private Image image4 = new Image(
            "app/img4",
            "youri4@hotmail.com",
            "Dat is mooi."

    );
    private Image image5 = new Image(
            "app/img5",
            "youri5@hotmail.com",
            "Dat is mooi."

    );

    @BeforeEach
    public void beforeAllTests() {
        imageRepository.deleteAll();
        imageRepository.save(image1);
        imageRepository.save(image2);
        imageRepository.save(image3);
        imageRepository.save(image4);
        imageRepository.save(image5);
    }

    @AfterEach
    public void afterAllTests() {
        //Watch out with deleteAll() methods when you have other data in the test database!
        imageRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();


    @Test
    public void givenImage_whenGetImages_thenReturnJsonImages() throws Exception {

        mockMvc.perform(get("/images", this.image1.getKey()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void givenImage_whenGetImageByKey_thenReturnJsonImage() throws Exception{

        mockMvc.perform(get("/images/{key}", this.image1.getKey()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source", is("app/img")))
                .andExpect(jsonPath("$.description", is("Dat is mooi.")))
                .andExpect(jsonPath("$.userEmail", is("youri1@hotmail.com")));
    }
    @Test
    public void givenImage_whenGetImageByEmail_thenReturnJsonImage() throws Exception{

        mockMvc.perform(get("/images/user/{userEmail}", this.image1.getUserEmail()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source", is("app/img")))
                .andExpect(jsonPath("$.description", is("Dat is mooi.")))
                .andExpect(jsonPath("$.userEmail", is("youri1@hotmail.com")));
    }

    @Test
    public void givenImage_whenPostImage_thenReturnJsonImage() throws Exception {
        Image image = new Image( "app/img1",
                "post@hotmail.com",
                "this is a post Title");

        mockMvc.perform(post("/images")
                .content(mapper.writeValueAsString(image))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source", is("app/img1")))
                .andExpect(jsonPath("$.description", is("this is a post Title")))
                .andExpect(jsonPath("$.userEmail", is("post@hotmail.com")));

    }

    @Test
    public void giveImage_whenPutImage_thenReturnJsonImage() throws Exception {

        image2.setSource("app/img1");
        image2.setDescription("this is a put Title");
        image2.setUserEmail("put@hotmail.com");

        mockMvc.perform(put("/images")
                .content(mapper.writeValueAsString(image2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source", is("app/img1")))
                .andExpect(jsonPath("$.description", is("this is a put Title")))
                .andExpect(jsonPath("$.userEmail", is("put@hotmail.com")));
    }


    @Test
    public void givenImage_whenDeleteImage_thenStatusOk() throws Exception {

        mockMvc.perform(delete("/images/{key}", image1.getKey())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoImage_whenDeleteImage_thenStatusNotFound() throws Exception {

        mockMvc.perform(delete("/images/{key}", "0bb58513b1b57a7c7f8963f001f9896705b1ab2a22e320e4eb0ea2a985084fa")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
