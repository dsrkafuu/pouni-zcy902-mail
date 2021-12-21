//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.njcci.util;

import java.util.UUID;

public class FileNameUtil {
    public FileNameUtil() {
    }

    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String getFileName(String fileOriginName) {
        StringBuilder newName = new StringBuilder();
        newName.append(UUID.randomUUID().toString().replace("-", ""));
        newName.append(getSuffix(fileOriginName));
        return newName.toString();
    }
}
