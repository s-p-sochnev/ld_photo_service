package org.spsochnev.photo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private IntegrationService integrationService;

    @GetMapping("/photos")
    public List<PhotoDTO> listPhotos() {
        return integrationService.listPhotos();
    }

    @GetMapping(value = "/photo", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getPhoto(@RequestParam String path) {
        return integrationService.getPhoto(path);
    }
}
