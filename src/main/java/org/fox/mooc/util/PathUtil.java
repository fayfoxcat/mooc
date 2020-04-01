package org.fox.mooc.util;

public class PathUtil {
	/**
	 * 返回项目图片的根路径
	 * @return String
	 */
//	public static String getImgBasePath(String basePath) {
//		//获取当前运行环境的系统类型(mac/windows/lunix)
//		String os = System.getProperty("os.name");
//		//针对不同类型的系统设置不同的存储路径
//		if(os.toLowerCase().startsWith("win")) {
//			basePath=basePath+"/src/main/webapp/resources/data";
//		}else {
//			basePath=basePath+"/src/main/webapp/resources/data";
//		}
//		//替换路径符号
//		return basePath;
//	}

	/**
	 * 用户资格证书存储路径
	 */
	public static String CertificatePath(String ImagePath) {
		return "/mooc/resources/data/auth/image/"+ImagePath+"/certificate/";
	}

	/**
	 * 用户头像存储路经
	 */
	public static String AuthHeaderPath(String ImagePath) {
		return "/mooc/resources/data/auth/image/"+ImagePath+"/header/";
	}

	/**
	 * 课程图片存储路径(课程id)
	 */
	public static String CoursePath(String ImagePath) {
		return "/mooc/resources/data/course/"+ImagePath+"/image/";
	}

	/**
	 * 课程视频存储路径（课程id）
	 */
	public static String CourseVideoPath(String VideoPath) {
		return "/mooc/resources/data/course/"+VideoPath+"/video/";
	}

	/**
	 * 网站轮播图存储路径
	 */
	public static String CourselPath() {
		return "/mooc/resources/data/coursel/image/";
	}
}
