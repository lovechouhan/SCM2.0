package com.example.scm.Vaidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageSizeValidator implements ConstraintValidator<MaxImageSize, MultipartFile> {
    private long maxSize;

    @Override
    public void initialize(MaxImageSize constraintAnnotation) {
        this.maxSize = constraintAnnotation.value() * 1024; // convert KB to bytes
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) return true; // handle required separately
        return file.getSize() <= maxSize;
    }
}