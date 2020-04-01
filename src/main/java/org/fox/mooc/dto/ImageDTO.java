package org.fox.mooc.dto;

import lombok.Data;

import java.io.InputStream;

/**
 * @Author by fairyfox
 * *2020/3/18-13:50
 */
@Data
public class ImageDTO {

    /**
     * 文件名
     */
    private String imageName;

    /**
     * 文件流
     */
    private InputStream image;

    public ImageDTO(String imageName,InputStream image) {
        this.imageName = imageName;
        this.image = image;
    }
}
