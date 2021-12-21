//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.njcci.util;

import com.njcci.error.BusinessException;
import com.njcci.error.EmBusinessError;
import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
    public FileUploadUtil() {
    }

    public static String uploadImage(MultipartFile file, String path, String fileName) throws IOException {
        String newName = FileNameUtil.getFileName(fileName);
        String realPath = path + "/" + newName;
        File dest = new File(realPath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        String url_path = newName;

        try {
            file.transferTo(dest);
            return url_path;
        } catch (IOException var8) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, var8.getMessage());
        }
    }
}
