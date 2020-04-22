package com.ai.commonUtils.fileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ai.commonUtils.dateTimeUtils.DateTimeUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


public class FileUtils {
    private FileUtils() {}
    private static Logger log = Logger.getLogger(FileUtils.class);


    /**
     * 调取windows下的exe文件
     *
     * @param exeFilePath
     */
    public static void invokeExeFile(String exeFilePath) {
        Runtime rn = Runtime.getRuntime();
        Process p = null;
        try {
            log.info("\u8c03\u7528" + " .exe " + "\u6587\u4ef6" + " : " + exeFilePath);
            p = rn.exec(exeFilePath);
            log.info("执行windows系统下的.exe文件");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param dir
     * @return
     * @Description 递归方法删除含有文件的文件夹，如果是空文件夹也可以删除
     */
    public static boolean deleteFolder(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteFolder(new File(dir, children[i]));
                if (!success) {
                    log.info("删除" + dir.getAbsolutePath() + "文件夹及其该文件夹下面的所有文件");
                    return false;

                }
            }
        } else {
            log.info(dir.getAbsolutePath() + "文件夹不存在");

        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 创建一个文件夹
     *
     * @param folderPath
     */
    public static void createFolder(String folderPath) {

        if (!new File(folderPath).isDirectory()) {
            new File(folderPath).mkdirs();
            log.info("创建一个文件夹" + folderPath);
        }
    }

    /**
     * @param folderPath
     * @param fileExtensionName 拓展名称，例如png、xml这些
     * @param keyWord           文件名称的关键字
     * @return
     * @Description 根据文件名中的关键字以及拓展名 获取指定文件夹下，所有文件的名字，包括后缀, 返回ArrayList
     */
    public static ArrayList<String> getFileNameByExtensionsAndKeyWord(String folderPath, final String fileExtensionName, final String keyWord) {
        ArrayList<String> arrayListFileName = new ArrayList();
        File file = new File(folderPath);
        File[] fileList = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.endsWith(fileExtensionName) && name.contains(keyWord)) {
                    return true;
                }
                return false;
            }
        });
        for (int i = 0; i < fileList.length; i++) {
            File f = fileList[i];

            arrayListFileName.add(f.getName());
        }
        return arrayListFileName;
    }

    /**
     * 获取文件夹下所有文件的文件名， 返回ArrayList<String>
     *
     * @param folderPath
     * @return
     */
    public static ArrayList<String> getFileNameUnderFolder(String folderPath) {
        ArrayList<String> arrayListFileName = new ArrayList();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        String[] names = folder.list();
        if (names == null) {
            try {
                throw new Exception("文件夹下文件名为空");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            arrayListFileName.addAll(Arrays.asList(names));
        }
        return arrayListFileName;
    }

    /**
     * @param folderPath
     * @param fileExtensionName
     * @param keyWord
     * @param regEx             这个是正则表达式，\\D, 为数字，其他的看需求
     * @return
     * @Description 获取指定path路径下文件夹下的 全部文件名称， 且只获取从index =0 至 第一个【正则】的上一个char字符结束！
     */
    public static ArrayList<String> fileName(String folderPath, final String fileExtensionName, final String keyWord, String regEx) {
        ArrayList<String> arrayListFileName = new ArrayList();
        File file = new File(folderPath);
        File[] fileList = file.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.endsWith(fileExtensionName) && name.contains(keyWord)) {
                    return true;
                }
                return false;
            }
        });
        for (int i = 0; i < fileList.length; i++) {
            File f = fileList[i];

            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(f.getName());
            String result = m.replaceAll("").trim();
            Character ch = result.charAt(0);
            int index = f.getName().indexOf(ch);
            String finalString = f.getName().substring(0, index - 1);

            arrayListFileName.add(finalString + fileExtensionName);
        }
        return arrayListFileName;
    }

    /**
     *将其文件拷贝到领一个文件夹下，linux_mac_Win可以忽略大小写传入，使用命令形式
     */
    public static void copyOneFile(String linux_mac_Win,String sourcePath,String fileNameWithExtands, String destationPath){
        Runtime run = Runtime.getRuntime();
        String sourcePathFile = sourcePath + File.separator + fileNameWithExtands;
        File sourFile = new File(sourcePathFile);
        String command = null;
        if (!new File(destationPath).isDirectory()) {
            new File(destationPath).mkdir();
        }
        try {
            if(linux_mac_Win.equalsIgnoreCase("linux")){
                run.exec("cd "+ sourcePath);
                command = "mv "+sourcePathFile +" "+destationPath;
            }else if(linux_mac_Win.equalsIgnoreCase("windows") || linux_mac_Win.equalsIgnoreCase("win")){
                command = "cmd /c copy  " + sourcePathFile + "  " + destationPath;
            }else if(linux_mac_Win.equalsIgnoreCase("mac")){
                run.exec("cd "+ sourcePath);
                command = "mv "+sourcePathFile +" "+destationPath;
            }
            run.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("拷贝文件" + sourcePathFile + " 至目标文件地址 " + destationPath);
    }

    /**
     *使用java的apicopy文件,src,target必须传入到文件级别，例如"/Users/macos/Desktop/测试/param.xlsx";
     */
    public static void copyOneFile(String srcFilePath,String targetFilePath){
        copyFile(srcFilePath,targetFilePath);
    }

    /**
     * 私有方法，为copyAllFile提供支持
     */
    private static void copyFile(String src, String target) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(target);
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = is.read(buff, 0, buff.length)) != -1) {
                os.write(buff, 0, len);
            }
            System.out.println("文件拷贝成功！");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 将一个路径下的所有文件 copy 到另外一个指定的文件夹目录下
     */
    public static void copyAllFile(String srcPath, String targetPath) {
        File f = new File(srcPath);
        File[] fileList = f.listFiles();
        if (!new File(targetPath).isDirectory()) {
            new File(targetPath).mkdir();
        }
        for (File f1 : fileList) {
            if (f1.isFile()) {
                copyFile(srcPath + File.separator + f1.getName(), targetPath + File.separator + f1.getName());
            }
            if (f1.isDirectory()) {
                copyAllFile(f1.getPath().toString(), targetPath + File.separator + f1.getName());
            }
        }
    }

    /**
     *获取当前路径，返回项目路径地址
     */
    public static String getCurrentProjectPath() {
        return System.getProperty("user.dir");
    }

    /**
     * 写入properties文件, 返回这个文件的生成路径
     */
    public static String writePropertiesFile(String filePath, String fileName, Map<String, String> maps) {
        Properties properties = new Properties();
        File file = new File(filePath + File.separator + fileName + ".properties");
        FileOutputStream fos = null;
        try {
            createFile(filePath, fileName + ".properties");
            for (String key : maps.keySet()) {
                properties.setProperty(key, maps.get(key));
            }
            fos = new FileOutputStream(file);
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("写入properties文件内容，该文件path:" + filePath + File.separator + fileName + ".properties");
        return filePath + File.separator + fileName + ".properties";
    }


    /**
     * 读取properties文件
     */
    public static String readPropertiesFile(String propertiesFilePath, String key) {
        Properties prop = null;
        FileInputStream fis = null;
        prop = System.getProperties();
        try {
            fis = new FileInputStream(new File(propertiesFilePath));
            prop.load(fis);
            log.info("读取.properties文件" + propertiesFilePath + "下的 key =" + key + ",对应的value = " + prop.getProperty(key));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop.getProperty(key);
    }

    /**
     * 读取txt文件,可设置encoding进行读取GBK编码，以防乱码，返回文件内容
     *
     * @param filePath
     * @param encoding
     * @return
     */
    public static String readTxtFile(String filePath, String encoding) {
        File file = new File(filePath);
        InputStreamReader read = null;
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        String lineTxt = "";
        StringBuilder content = new StringBuilder();
        try {
            // 判断文件是否存在
            if (file.isFile() && file.exists()) {
                fileInputStream = new FileInputStream(file);
                // 考虑到编码格式
                read = new InputStreamReader(fileInputStream,encoding);
                bufferedReader = new BufferedReader(read);
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    content.append(lineTxt);
                }
                return content.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(read != null){
                    read.close();
                }
                if(fileInputStream != null){
                    fileInputStream.close();
                }
                if(bufferedReader != null){
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";

    }

    /**
     * 读取txt文件的内容，filePath要求为xxxx/dd.txt,返回.txt文件里的内容
     */
    public static String readTxtFile(String filePath){
        // 读取txt内容为字符串
        StringBuffer txtContent = new StringBuffer();
        // 每次读取的byte数
        byte[] b = new byte[8 * 1024];
        InputStream in = null;
        try {
            // 文件输入流
            in = new FileInputStream(filePath);

            while (in.read(b) != -1) {
                // 字符串拼接
                txtContent.append(new String(b));

            }
            // 关闭流
            in.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return txtContent.toString();
    }

    /**
     * 向txt文件中写数据，flag如果是true为追加，如果是false为覆盖，返回文件地址
     */
    public static String writeIntoTxtFile(String txtFilePath, String str, boolean flag){
        File file = new File(txtFilePath);
        FileWriter fw = null;
        PrintWriter pw = new PrintWriter(fw);
        try {
            fw = new FileWriter(file, flag);
            pw.println(str);
            pw.flush();
            fw.flush();
            if (flag) {
                log.info("向txt文件" + txtFilePath + "中追加写入数据" + str);
            } else {
                log.info("向txt文件覆盖内容，" + txtFilePath + "写入数据" + str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(pw != null){
                pw.close();
            }
            if(fw != null){
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return txtFilePath;

    }

    /**
     *创建文件,返回该文件夹的路径
     * @param fileNameAndExtension 传入文件名.后缀名
     */
    public static String createFile(String filePath, String fileNameAndExtension) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
            log.info(filePath + "不存在， 创建一个文件夹");
        }
        File f = new File(filePath, fileNameAndExtension);
        if (!f.exists()) {
            try {
                f.createNewFile();
                log.info("创建一个新的文件" + filePath + fileNameAndExtension);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return filePath + fileNameAndExtension;
    }

    /**
     *获取文件地址的名称，不含包后缀名，例如/Users/macos/Desktop/param.xls,获取/Users/macos/Desktop/param
     */
    public static String getFileNameExcludeExtendsName(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0) {
            return fileName;
        }
        String filePffixName = fileName.substring(0, dotIndex);
        if ("".equals(filePffixName)){
            return fileName;}
        else{
            return filePffixName;}
    }


    /**
     * 获取文件名的后缀名
     * @param fileName
     * @return
     */
    public static String getFileExtandsName(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取文件最后修改日期
     * @param filePath
     * @return
     */
    public static Date getLastModifyFileDate(String filePath){
        File file = new File(filePath);
        Calendar calendar = Calendar.getInstance();
        long time = file.lastModified();
        calendar.setTimeInMillis(time);
        return calendar.getTime();
    }


    /**
     * 获取文件的大小
     * @param returnB_KB_MB_GB 可以忽略大小写传入 B、KB、MB、GB
     * @return
     */
    public static String getFileSize(String filePath,String returnB_KB_MB_GB){
        File file = new File(filePath);
        //得到的大小是B的单位
        long size = file.length();
        String result = null;
        System.out.println(size);
        if(returnB_KB_MB_GB.equalsIgnoreCase("B")){
            result  = String.valueOf(size)+"B";
        }else if(returnB_KB_MB_GB.equalsIgnoreCase("KB")){
            result = String.valueOf(size / 1024)+"KB";
        }else if(returnB_KB_MB_GB.equalsIgnoreCase("MB")){
            result = String.valueOf(size / (1024 * 1024)) + "MB";
        }else if(returnB_KB_MB_GB.equalsIgnoreCase("GB")){
            result = String.valueOf(size / (1024 * 1024 * 1024)) + "GB";
        }
        return result;
    }

    public static void main(String[] args) {
        String path = "/Users/macos/Desktop/param.xlsx";
        String target = "/Users/macos/Desktop/测试/param.xlsx";
        copyOneFile(target,path);


    }



}
