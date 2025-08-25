package com.example.scm.Services.DefinationFolder;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile contactIMG, String filename);

    String getURLfromPublicID(String publicId);

}
