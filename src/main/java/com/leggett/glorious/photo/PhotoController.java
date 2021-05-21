package com.leggett.glorious.photo;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PhotoController {
    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping(path = "/photos")
    public @ResponseBody Iterable<Photo> getPhotos(int page, int size) {
        Pageable imagePage = PageRequest.of(page, size, Sort.Direction.DESC, "dateTaken");
        Page<Photo> photosPage = photoRepository.findAll(imagePage);
        List<Photo> photosList = photosPage.getContent();
        return photosList;
    }

    @GetMapping(value = "/getPhoto", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getPhotoById(Long id) throws IOException {
        Optional<Photo> photo = photoRepository.findById(id);
        File file = new File(photo.get().getFileName());
        int length = (int) (file.length());
        FileInputStream fin = new FileInputStream(file);
        DataInputStream in = new DataInputStream(fin);
        byte[] bytecodes = new byte[length];
        in.readFully(bytecodes);
        in.close();
        return bytecodes;
    }
}
