/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author legia
 */

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

public class CloudinaryController {
    // Khởi tạo Cloudinary 
    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
        "cloud_name", "dnzc9p3jj",
        "api_key", "199637772186358",
        "api_secret", "JcmmcMjcJxr7z8cTo1OSO759wNk"
    ));

    public static String uploadToCloudinary(InputStream inputStream) throws Exception {
        // Convert InputStream to byte[]
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        byte[] fileBytes = buffer.toByteArray();

        // Upload to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap(
            "resource_type", "auto"
        ));
        return (String) uploadResult.get("url");
    }
}
