package com.gin.admin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
	/**
	 * 遍历文件夹并复制
	 *
	 * @param sourse
	 * @param target
	 */
	public static void Clone(String source, String target) {
		File targetDir = new File(target);
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		// 获取目录下所有文件
		File f = new File(source);
		File[] allf = f.listFiles();
		// 遍历所有文件
		for (File fi : allf) {
			try {
				// 拼接目标位置
				String URL = target + fi.getAbsolutePath().substring(source.length());
				// 创建目录或文件
				if (fi.isDirectory()) {
					File dir = new File(URL);
					if (!dir.exists()) {
						dir.mkdirs();
					}
				} else {
					fileInputOutput(fi.getAbsolutePath(), URL);
				}
				// 递归调用
				if (fi.isDirectory()) {
					Clone(fi.getAbsolutePath(), URL);
				}
			} catch (Exception e) {
				System.out.println("error");
			}
		}
	}

	/**
	 * 复制文件
	 *
	 * @param sourse
	 * @param target
	 */
	public static void fileInputOutput(String sourse, String target) {
		try {
			File s = new File(sourse);
			File t = new File(target);
			FileInputStream fin = new FileInputStream(s);
			FileOutputStream fout = new FileOutputStream(t);
			byte[] a = new byte[1024 * 1024 * 4];
			int b = -1;
			// 边读边写
			while ((b = fin.read(a)) != -1) {
				fout.write(a, 0, b);
			}
			fout.close();
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 删除文件夹
	 * </p>
	 *
	 * @author o1760
	 * @date 2019年8月6日
	 * @param folderPath 文件夹目录
	 * @param included   是否包含自己
	 */
	public static void delFolder(String folderPath, boolean included) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			if (included) {
				File myFilePath = new File(folderPath);
				myFilePath.delete(); // 删除空文件夹
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 递归删除
	private static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (String element : tempList) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + element);
			} else {
				temp = new File(path + File.separator + element);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + element);// 先删除文件夹里面的文件
				delFolder(path + "/" + element, true);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
}
