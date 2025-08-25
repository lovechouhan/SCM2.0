package com.example.scm.Services.Implementations;

import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.example.scm.Services.DefinationFolder.ImageService;
import com.example.scm.helper.AppConstants;

@Service
public class ImageServiceImpl implements ImageService {

    private Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile contactIMG, String filename) {
         // woh code likhna hai jo image ko server/cloud par upload kr rha hai
             
         
 
    



        try {
         
            byte[] dataa = new byte[contactIMG.getInputStream().available()];
            contactIMG.getInputStream().read(dataa);
            cloudinary.uploader().upload(dataa, ObjectUtils.asMap("public_id", filename));

            
            return this.getURLfromPublicID(filename);


        } catch (IOException ex) {

            ex.printStackTrace();
            return null;
        }
 

        // return krega URL

        
    }

    @Override
    public String getURLfromPublicID(String publicId) {
        return cloudinary
        .url()
        .transformation(
            new Transformation<>()
            .width(AppConstants.CONTACT_IMG_WIDTH)
            .height(AppConstants.CONTACT_IMG_HEIGHT)
            .crop(AppConstants.CONTACT_IMG_CROP)
        )
        .generate(publicId);
    }

}
