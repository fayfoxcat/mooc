package org.fox.mooc.controller.teachers;

import org.fox.mooc.entity.course.CourseSection;
import org.fox.mooc.service.course.CourseSectionService;
import org.fox.mooc.util.ImageUtil;
import org.fox.mooc.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * 分片上传Controller
 */
@Controller
@RequestMapping("/upload")
public class VideoController {

    @Autowired
    private CourseSectionService courseSectionService;

    /**
     * 上传临时目录
     */
    private static String uploadPath = ImageUtil.basePath+File.separator + "temp";

    /**
     * 跳转到首页
     *
     * @return
     */
    @GetMapping("index")
    public String toUpload() {
        return "/upload";
    }

    /**
     * 查看当前分片是否上传
     *
     * @param request
     * @param response
     */
    @PostMapping("checkblock")
    @ResponseBody
    public void checkMd5(HttpServletRequest request, HttpServletResponse response) {
        //当前分片
        String chunk = request.getParameter("chunk");
        //分片大小
        String chunkSize = request.getParameter("chunkSize");
        //当前文件的MD5值
        String guid = request.getParameter("guid");
        //分片上传路径
        String tempPath = uploadPath;
        File checkFile = new File(tempPath + File.separator + guid + File.separator + chunk);
        response.setContentType("text/html;charset=utf-8");
        try {
            //如果当前分片存在，并且长度等于上传的大小
            if (checkFile.exists() && checkFile.length() == Integer.parseInt(chunkSize)) {
                response.getWriter().write("{\"ifExist\":1}");
            } else {
                response.getWriter().write("{\"ifExist\":0}");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传分片
     *
     * @param file
     * @param chunk
     * @param guid
     * @throws IOException
     */
    @PostMapping("save")
    @ResponseBody
    public void upload(@RequestParam MultipartFile file, Integer chunk, String guid) throws IOException {
        String filePath = uploadPath + File.separator + guid;
        File tempfile = new File(filePath);
        if (!tempfile.exists()) {
            tempfile.mkdirs();
        }
        RandomAccessFile raFile = null;
        BufferedInputStream inputStream = null;
        if (chunk == null) {
            chunk = 0;
        }
        try {
            File dirFile = new File(filePath, String.valueOf(chunk));
            //以读写的方式打开目标文件
            raFile = new RandomAccessFile(dirFile, "rw");
            raFile.seek(raFile.length());
            inputStream = new BufferedInputStream(file.getInputStream());
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buf)) != -1) {
                raFile.write(buf, 0, length);
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (raFile != null) {
                raFile.close();
            }
        }
    }

    /**
     * 合并文件
     *
     * @param guid
     * @param fileName
     */
    @PostMapping("combine")
    @ResponseBody
    public void combineBlock(String guid, String fileName) {
        //获取文件名中的sectionId
        long sectionId =Long.parseLong(fileName.substring(0, fileName.indexOf(".")));
        //查询对应courseId
        Long courseId = courseSectionService.getById(sectionId).getCourseId();
        //拼接数据库存储路径(相对路径)
        String relativePath = PathUtil.CourseVideoPath(courseId.toString())+fileName;
        //拼接文件存储目录
        String dest = ImageUtil.basePath+createPath(courseId.toString());
        //拼接文件存储路径
        String path = dest+fileName;
        //分片文件临时目录
        File tempPath = new File(uploadPath + File.separator + guid);
        //真实上传路径
        File realPath = new File(dest);
        if (!realPath.exists()) {
            realPath.mkdirs();
        }
        File realFile = new File(path);
        FileOutputStream os = null;// 文件追加写入
        FileChannel fcin = null;
        FileChannel fcout = null;
        try {
            System.out.println("合并文件——开始 [ 文件名称：" + fileName + " ，MD5值：" + guid + " ]");
            os = new FileOutputStream(realFile, true);
            fcout = os.getChannel();
            if (tempPath.exists()) {
                //获取临时目录下的所有文件
                File[] tempFiles = tempPath.listFiles();
                //按名称排序
                Arrays.sort(tempFiles, (o1, o2) -> {
                    if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
                        return -1;
                    }
                    if (Integer.parseInt(o1.getName()) == Integer.parseInt(o2.getName())) {
                        return 0;
                    }
                    return 1;
                });
                //每次读取10MB大小，字节读取
                //byte[] byt = new byte[10 * 1024 * 1024];
                //int len;
                //设置缓冲区为10MB
                ByteBuffer buffer = ByteBuffer.allocate(10 * 1024 * 1024);
                for (int i = 0; i < tempFiles.length; i++) {
                    FileInputStream fis = new FileInputStream(tempFiles[i]);
                    /*while ((len = fis.read(byt)) != -1) {
                        os.write(byt, 0, len);
                    }*/
                    fcin = fis.getChannel();
                    if (fcin.read(buffer) != -1) {
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            fcout.write(buffer);
                        }
                    }
                    buffer.clear();
                    fis.close();
                    //删除分片
                    tempFiles[i].delete();
                }
                os.close();
                //删除临时目录
                if (tempPath.isDirectory() && tempPath.exists()) {
                    System.gc(); // 回收资源
                    tempPath.delete();
                }
                //文件上传成功，写入数据库
                CourseSection courseSection = new CourseSection();
                courseSection.setId(sectionId);
                courseSection.setVideoUrl(relativePath);
                courseSectionService.updateSelectivity(courseSection);
                System.out.println("文件合并——结束 [ 文件名称：" + fileName + " ，MD5值：" + guid + " ]");
            }
        } catch (Exception e) {
            System.out.println("文件合并——失败 " + e.getMessage());
        }
    }

    /*生成文件相对路径（不含扩展名）*/
    private static String createPath(String fileName){
        //生成对应课程路径
        String addrPath = PathUtil.CourseVideoPath(fileName);
        //项目视频子路径(写入数据库)
        return addrPath.substring(addrPath.indexOf("data")+4);
    }
}
