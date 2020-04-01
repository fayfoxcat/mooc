package org.fox.mooc.util;

import org.fox.mooc.BaseTest;
import org.fox.mooc.dto.ImageDTO;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class ImageUtilTest extends BaseTest{
    @Test
    public void testQueryPage() {
       String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
       String path = basePath.substring(basePath.indexOf("IDEA")+4);
       System.out.println(path);
    }

}
