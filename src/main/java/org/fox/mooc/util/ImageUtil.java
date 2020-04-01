package org.fox.mooc.util;

import org.fox.mooc.dto.ImageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class ImageUtil {
	//获取项目根路径
	private static String base = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	public static String basePath = base.substring(1,base.indexOf("mooc")+4)+"/src/main/webapp/resources/data";
	//获取格式化时间对象
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	//获取随机数对象
	private static final Random r = new Random();

	//日志
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	/**
	 * 1.将图片存储到项目指定目录下
	 * 2.重命名图片名，并归类
	 * @return
	 */
	public static String ImagePath(ImageDTO imageDTO, String addrPath) throws IOException {
		//图片子路径
		String targetAddr = addrPath.substring(addrPath.indexOf("data")+4);
		//使用随机数生成 图片名
		String realFileName = getRandomFileName();
		//获取文件图片扩展名
		String extension = getFileExtension(imageDTO.getImageName());
		//创建图片存储的绝对路径(相对于项目图片存储根路径)
		makeDirPath(targetAddr);
		//磁盘图片子路径
		String relativeAddr = targetAddr+realFileName+extension;
		//项目图片子路径
		String projectRelativeAddr = addrPath+realFileName+extension;
		//图片绝对路径（全路径）
		File dest = new File(basePath+relativeAddr);
		//输出当前文件存储全路径
		logger.info(String.valueOf(dest));
		InputStream  in = null;
		OutputStream out = null;
		try{
			//获取文件输入流
			in = imageDTO.getImage();
			//获取文件输出流并将文件输出到指定目录
			out = new FileOutputStream(dest);

			byte[] buffer = new byte[10000];
			int len = -1;
			while((len = in.read(buffer)) != -1) {
				out.write(buffer,0,len);
			}
		} catch(IOException e) {
			e.printStackTrace();
			logger.error("写入文件报错信息："+e.getMessage());
		} finally {
			if(in!=null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out!=null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return projectRelativeAddr;
	}


	/**
	 * 创建目标路径所涉及的目录将自动创建
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = basePath+targetAddr;
		File dirPath = new File(realFileParentPath);
		//该路径不存在将被创建
		if(!dirPath.exists()) {
			boolean mkdirs = dirPath.mkdirs();
		}
	}

	/**
	 * 获取文件输入流的扩展名
	 * @param fileName
	 * @return String
	 */
	private static String getFileExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	/**
	 * 生成随机文件名，当前的年/月/日/时/分/秒+五位随机数
	 * @return String
	 */
	public static String getRandomFileName() {
		//获取五位随机数
		int rannum = r.nextInt(89999)+10000;
		//将时间格式化为字符串
		String nowTimeStr = sDateFormat.format(new Date());
		//自动转换为String返回
		return nowTimeStr+rannum;
	}
	
	/**
	 * 判断storePath是文件路径还是目录路径
	 * 1.如果是文件路径则删除该文件
	 * 2.如果是目录路径则删除该目录下的所有文件
	 */
	public static void deleteFileOrPath(String storePath) {
		//拼凑出图片的全路径
		File fileOrPath = new File((basePath+storePath).replace("\\","/"));
		if(fileOrPath.exists()) {
			//如果是目录
			if(fileOrPath.isDirectory()) {
				//将文件目录转换为数组，并遍历删除
				File[] files = fileOrPath.listFiles();
				for (File file : files) {
					file.delete();
				}
			}
			fileOrPath.delete();
		}
	}

	/**
	 *构建文件对象
	 */
	public static ImageDTO Image(HttpServletRequest request, ImageDTO imageDTO) throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		//扫描文件、构建ImageDTO对象
		CommonsMultipartFile uploadFile = (CommonsMultipartFile) multipartRequest.getFile("file");
		if(uploadFile != null) {
			imageDTO = new ImageDTO(uploadFile.getOriginalFilename(),uploadFile.getInputStream());
		}
		return imageDTO;
	}
}














