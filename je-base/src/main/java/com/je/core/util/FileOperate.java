package com.je.core.util;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import com.je.cache.service.doc.FileTypeCacheManager;
import com.je.core.entity.extjs.JSONTreeNode;
import com.je.dd.vo.DicInfoVo;
import com.je.document.vo.FileType;

/**
 * 文件操作工具类
 * @author 研发部:云凤程
 *
 */
public class FileOperate {

    static String message;

    public FileOperate() {
    }

    /**
     * 读取文本文件内容
     *
     * @param filePathAndName
     *            带有完整绝对路径的文件名
     * @param encoding
     *            文本文件打开的编码方式
     * @param hh
     *            换行符
     * @return 返回文本文件的内容
     */
    public static String readTxt(String filePathAndName, String encoding,String hh) {
        encoding = encoding.trim();
        StringBuffer str = new StringBuffer();
        FileInputStream fs=null;
        InputStreamReader isr=null;
        BufferedReader br=null;
        try {
            fs= new FileInputStream(filePathAndName);
            if (encoding.equals("")) {
                isr = new InputStreamReader(fs);
            } else {
                isr = new InputStreamReader(fs, encoding);
            }
             br = new BufferedReader(isr);
                String data = "";
                while ((data = br.readLine()) != null) {
                    str.append(data + (hh==null?" ":hh));
                }
        } catch (IOException es) {
            throw new PlatformException("工具类文件读取文件内容失败", PlatformExceptionEnum.JE_CORE_UTIL_FILE_READTEXT_ERROR,new Object[]{filePathAndName,encoding,hh},es);
        }finally {
            close(fs,null,isr,br);
        }
        return str.toString();
    }
    public static void close(InputStream fs, OutputStream os, InputStreamReader isr, BufferedReader br){
        if(fs!=null){
            try {
                fs.close();
            } catch (IOException e) {
                throw new PlatformException("工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CLOSEIO_ERROR,e);
            }
        }
        if(os!=null){
            try {
                os.close();
            } catch (IOException e) {
                throw new PlatformException("工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CLOSEIO_ERROR,e);
            }
        }
        if(isr!=null){
            try {
                isr.close();
            } catch (IOException e) {
                throw new PlatformException("工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CLOSEIO_ERROR,e);
            }
        }
        if(br!=null){
            try {
                br.close();
            } catch (IOException e) {
                throw new PlatformException("工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CLOSEIO_ERROR,e);
            }
        }
    }
    public static String resolveCode(String path) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path);
            byte[] head = new byte[3];
            inputStream.read(head);
            String code = "gb2312";  //或GBK
            if (head[0] == -1 && head[1] == -2 ){
                code = "UTF-16";
            }else if (head[0] == -2 && head[1] == -1 ){
                code = "Unicode";
            }else if(head[0]==-17 && head[1]==-69 && head[2] ==-65){
                code = "UTF-8";
            }
            close(inputStream,null,null,null);
            return code;
        } catch (Exception e) {
           throw new PlatformException("获取文件编码失败!",PlatformExceptionEnum.JE_CORE_UTIL_FILE_READ_ERROR,new Object[]{path},e);
        }finally {
            close(inputStream,null,null,null);
        }
    }
    /**
     * 读取文件成数组
     *
     * @param filePath
     *            带有完整绝对路径的文件名
     */
    public static byte[] readByte(String filePath) {
        byte[] docBytes = null;
        FileInputStream fis=null;
        ByteArrayOutputStream bos=null;
        try {
            File readFile = new File(filePath);
            fis= new FileInputStream(readFile);
            int len = 0;
            byte[] readBytes = new byte[1024];
             bos= new ByteArrayOutputStream();
            while((len = fis.read(readBytes)) != -1){
                bos.write(readBytes, 0, len);
            }
            docBytes = bos.toByteArray();
//    		FileOutputStream f=new FileOutputStream(new File("D:/text.docx"));
//    		f.write(docBytes);
//    		f.flush();
//    		f.close();
        } catch (FileNotFoundException e) {
            throw new PlatformException("文件工具类文件未找到异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_NOTFOUND_ERROR,new Object[]{filePath},e);
        } catch (IOException e) {
            throw new PlatformException("文件工具类读取文件异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_READ_ERROR,new Object[]{filePath},e);
        }finally {
            close(fis,bos,null,null);
        }
        return docBytes;
    }
    public static Map<String,String> readProperties(String filePath){
        Map<String,String> values=new HashMap<String,String>();
        InputStream inputStream=null;
        try{
            Properties properties = new Properties();
            inputStream = new FileInputStream(filePath);
            properties.load(inputStream);
//            inputStream.close(); //关闭流
            //	        chinese = new String(chinese.getBytes("ISO-8859-1"), "UTF-8"); // 处理中文乱码
            for(Object key:properties.keySet()){
                String keyStr=key.toString();
                values.put(key.toString(), properties.getProperty(keyStr));
            }
        }catch(Exception e){
            throw new PlatformException("文件工具类读取文件异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_READ_ERROR,new Object[]{filePath},e);
        }finally {
            close(inputStream,null,null,null);
        }
        return values;
    }

    /**
     * 新建目录
     *
     * @param folderPath
     *            目录
     * @return 返回目录创建后的路径
     */
    public static String createFolder(String folderPath) {
        String txt = folderPath;
        try {
            File myFilePath = new File(txt);
            txt = folderPath;
            if (!myFilePath.exists()) {
                myFilePath.mkdirs();
            }
        } catch (Exception e) {
            throw new PlatformException("文件工具类创建目录异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CREATEFOLDER_ERROR,new Object[]{folderPath},e);
        }
        return txt;
    }

    /**
     * 多级目录创建
     *
     * @param folderPath
     *            准备要在本级目录下创建新目录的目录路径 例如 c:myf
     * @param paths
     *            无限级目录参数，各级目录以单数线区分 例如 a|b|c
     * @return 返回创建文件后的路径 例如 c:myfac
     */
    public static String createFolders(String folderPath, String paths) {
        String txts = folderPath;
        try {
            String txt;
            txts = folderPath;
            StringTokenizer st = new StringTokenizer(paths, "|");
            for (int i = 0; st.hasMoreTokens(); i++) {
                txt = st.nextToken().trim();
                if (txts.lastIndexOf("/") != -1) {
                    txts = createFolder(txts + txt);
                } else {
                    txts = createFolder(txts + txt + "/");
                }
            }
        } catch (Exception e) {
            throw new PlatformException("文件工具类创建目录异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CREATEFOLDER_ERROR,new Object[]{folderPath,paths},e);
        }
        return txts;
    }

    /**
     * 新建文件
     *
     * @param filePathAndName
     *            文本文件完整绝对路径及文件名
     * @param fileContent
     *            文本文件内容
     * @return
     */
    public static void createFile(String filePathAndName, String fileContent) {
        PrintWriter myFile=null;
        FileWriter resultFile=null;
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            resultFile = new FileWriter(myFilePath);
            myFile = new PrintWriter(resultFile);
            String strContent = fileContent;
            myFile.println(strContent);
//            myFile.close();
//            resultFile.close();
        } catch (Exception e) {
            throw new PlatformException("文件工具类创建文件异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CREATEFILE_ERROR,new Object[]{filePathAndName,fileContent},e);
        }finally {
            if(resultFile!=null){
                try {
                    resultFile.close();
                } catch (IOException e) {
                    throw new PlatformException("文件工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CLOSEIO_ERROR,new Object[]{filePathAndName,fileContent},e);
                }
            }
            if(myFile!=null){
                try {
                    myFile.close();
                } catch (Exception e) {
                    throw new PlatformException("文件工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CLOSEIO_ERROR,new Object[]{filePathAndName,fileContent},e);
                }
            }

        }
    }

    /**
     * 有编码方式的文件创建
     *
     * @param filePathAndName
     *            文本文件完整绝对路径及文件名
     * @param fileContent
     *            文本文件内容
     * @param encoding
     *            编码方式 例如 GBK 或者 UTF-8
     * @return
     */
    public static void createFile(String filePathAndName, String fileContent,
                                  String encoding) {
        PrintWriter myFile=null;
        try {
            String filePath = filePathAndName;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();
            }
            myFile = new PrintWriter(myFilePath, encoding);
            String strContent = fileContent;
            myFile.println(strContent);
//            myFile.close();
        } catch (Exception e) {
            throw new PlatformException("文件工具类创建文件异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CREATEFILE_ERROR,new Object[]{filePathAndName,fileContent,encoding},e);
        }finally {
            if(myFile!=null){
                try {
                    myFile.close();
                } catch (Exception e) {
                    throw new PlatformException("文件工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CLOSEIO_ERROR,new Object[]{filePathAndName,fileContent},e);
                }
            }
        }
    }

    /**
     * 创建普通文件
     * @param path
     * @return
     */
    public static File createFile(String path){
        //判断文件是否存在，如果不存在就创建新的
        File file = new File(path);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new PlatformException("文件工具类创建文件异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CREATEFILE_ERROR,new Object[]{path},e);
        }
        return file;
    }

    /**
     * 判断文件是否存在
     * @author linzhichao 2012-06-27
     * @param filePath
     * @return
     */
    public static boolean existsFile(String filePath){
        boolean exists = false;
        try {
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            exists = myFilePath.exists();
        } catch (Exception e) {
            throw new PlatformException("文件工具类校验文件是否存在异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_EXISTSFILE_ERROR,new Object[]{filePath},e);
        }
        return exists;
    }

    /**
     * 删除文件
     *
     * @param filePathAndName
     *            文本文件完整绝对路径及文件名
     * @return Boolean 成功删除返回true遭遇异常返回false
     */
    public static boolean delFile(String filePathAndName) {
        boolean bea = false;
        try {
            String filePath = filePathAndName;
            File myDelFile = new File(filePath);
            if (myDelFile.exists()) {
                myDelFile.delete();
                bea = true;
            } else {
                bea = false;
                //throw new PlatformException("文件工具类文件未找到异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_NOTFOUND_ERROR,new Object[]{filePath});
            }
        } catch (Exception e) {
            throw new PlatformException("文件工具类删除文件异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_DELETEFILE_ERROR,new Object[]{filePathAndName},e);
        }
        return bea;
    }

    /**
     * 删除文件夹
     * @param folderPath
     *            文件夹完整绝对路径
     * @return
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            throw new PlatformException("文件工具类删除文件异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_DELETEFOLDER_ERROR,new Object[]{folderPath},e);
        }
    }

    /**
     * 删除指定文件夹下所有文件
     *
     * @param path
     *            文件夹完整绝对路径
     * @return
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean bea = false;
        File file = new File(path);
        if (!file.exists()) {
            return bea;
        }
        if (!file.isDirectory()) {
            return bea;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
                bea = true;
            }
        }
        return bea;
    }

    /**
     * 复制单个文件
     *
     * @param oldPathFile
     *            准备复制的文件源
     * @param newPathFile
     *            拷贝到新绝对路径带文件名
     * @return
     */
    public static void copyFile(String oldPathFile, String newPathFile) {
        File oldfile = new File(oldPathFile);
        if(!oldfile.exists()){return;}
        File newFile=new File(newPathFile);
        //如果文件与目标文件一致则不复制操作
        if(oldfile.getPath().equalsIgnoreCase(newFile.getPath())){
          return;
        }
        copyFile(oldfile, newFile, true);
    }

    /**
     * 复制文件        在原目录下， 重新生成新
     * @param file
     * @param fileFullPath
     */
    public static void copyFileFullPath(File file,String fileFullPath){
        File dirfile=new File(fileFullPath);
        File parentFile=dirfile.getParentFile();

        if(!parentFile.exists()){
            parentFile.mkdirs();
        }

        //如果复制的文件跟目标一致则不操作
        if(dirfile.getAbsolutePath().equals(file.getAbsolutePath())){
            return;
        }

        FileOperate.copyFile(file, dirfile,true);
    }

    /**
     * 文件复制
     * @param src
     * @param dst
     * @param overwrite
     */
    public static void copyFile(File src, File dst, boolean overwrite)  {
        if(dst.exists() && overwrite) {
            dst.delete();
        }
        copyFileNew(src, dst);
    }
    /**
     * 文件复制   采用文件通道复制      速度比原来提升很高， 但是稳定性有待测试，  这次改动  主要为了解决 原有文件复制问题，  当txt  文字类型文件被复制。。  后面会多无效的乱字符
     * @param s
     * @param t
     */
    public static void copyFileNew(File s,File t){
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            throw new PlatformException("文件工具类复制文件异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_COPYFILE_ERROR,new Object[]{s.getPath(),t.getPath()},e);
        } finally {
            close(fi,fo,null,null);
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    throw new PlatformException("文件工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CLOSEIO_ERROR,new Object[]{s.getPath(),t.getPath()},e);
                }
            }
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    throw new PlatformException("文件工具类关闭文件流异常", PlatformExceptionEnum.JE_CORE_UTIL_FILE_CLOSEIO_ERROR,new Object[]{s.getPath(),t.getPath()},e);
                }
            }
        }
    }
    /******
     *
     * @param oldPathFile
     * @param newPathFile
     * @param filename
     */
    public static void copyFile(String oldPathFile, String newPathFile,String filename) {
        File oldfile = new File(oldPathFile);
        newPathFile+=filename;
        File newFile=new File(newPathFile);
        copyFile(oldfile, newFile, true);
    }

    /**
     * 复制整个文件夹的内容
     *
     * @param oldPath
     *            准备拷贝的目录
     * @param newPath
     *            指定绝对路径的新目录
     * @return
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            new File(newPath).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input=null;
                    FileOutputStream output=null;
                    try {
                        input = new FileInputStream(temp);
                        output = new FileOutputStream(newPath
                                + "/" + (temp.getName()).toString());
                        byte[] b = new byte[1024 * 5];
                        int len;
                        while ((len = input.read(b)) != -1) {
                            output.write(b, 0, len);
                        }
                        output.flush();
                    }catch (Exception ie){
                        throw new PlatformException("文件工具类复制文件异常",PlatformExceptionEnum.JE_CORE_UTIL_FILE_COPYFILE_ERROR,new Object[]{oldPath+file[i],newPath},ie);
                    }finally {
                        close(input,output,null,null);
                    }
                }
                if (temp.isDirectory()) {// 如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            throw new PlatformException("文件工具类复制文件夹异常",PlatformExceptionEnum.JE_CORE_UTIL_FILE_COPYFOLDER_ERROR,new Object[]{oldPath,newPath},e);
//            message = "复制整个文件夹内容操作出错";
        }
    }

    /**
     * 移动文件
     *
     * @param oldPath
     * @param newPath
     * @return
     */
    public static void moveFile(String oldPath, String newPath) {
        copyFile(oldPath, newPath);
        delFile(oldPath);
    }

    /**
     * 移动目录
     *
     * @param oldPath
     * @param newPath
     * @return
     */
    public static void moveFolder(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delFolder(oldPath);
    }

    public static String getMessage() {
        return message;
    }

    /**
     * 根据id来确定图片放置路径
     *
     * @param infoId - 数字编号
     * @return 分隔目录
     */
    public static String getPathById(int infoId) {
        return getPathById(infoId+"");
    }

    /**
     * 根据id来确定图片放置路径
     *
     * @param infoId - 字符编号
     * @return 分隔目录
     */
    public static String getPathById(String infoId) {
        StringBuffer path = new StringBuffer();
        while (infoId.length() > 1) {
            path.append(infoId.substring(0, 2)).append("/");
            infoId = infoId.substring(2);
        }

        if (infoId.length() > 0){
            path.append(infoId).append("/");
        }

        return path.toString();
    }
    /**
     * 写入文件流
     * @param text
     * @param file
     */
    public static void writeText(String text, File file){
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write(text);
            output.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new PlatformException("文件工具类写入文件流异常",PlatformExceptionEnum.JE_CORE_UTIL_FILE_COPYFOLDER_ERROR,new Object[]{text,file.getPath()},e);
        }
    }

    /**
     * 获取文件目录的树形
     * @param file
     * @param basePath
     * @return
     */
    public static JSONTreeNode getFilesTree(File file, String basePath,DicInfoVo dicInfoVo){
        String name = file.getName();
        if (".svn".equals(name)) {
            return null;
        }
        String path = file.getAbsolutePath().replace(basePath, "");
        path = path.length() > 0 ? path.substring(1) : path;
        //解决linux问题，将\\变换为/
        if(!"/".equals(File.separator)){
            path=path.replaceAll("\\\\", "/");
        }
        JSONTreeNode node=TreeUtil.buildTreeNode(path+dicInfoVo.getIdSuffix(), name, path, dicInfoVo.getFieldCode(), "", "", "");
        node.setLeaf(!file.isDirectory());
        node.setExpandable(true);
        if(node.isLeaf()){
            node.setNodeInfoType("FOLDER");
            node.setDisabled("0");
        }else{
            node.setNodeInfoType("FILE");
            node.setDisabled("1");
        }
        if (file.isDirectory()) {
            node.setIconCls("jeicon jeicon-folder");
            File[] files = file.listFiles();
            for (File f : files) {
                JSONTreeNode childNode = getFilesTree(f, basePath,dicInfoVo);
                if(childNode!=null){
                    childNode.setParent(node.getId()+dicInfoVo.getIdSuffix());
                    node.getChildren().add(childNode);
                }
            }
        } else {
            String fileType=name.substring(name.lastIndexOf(".")+1);
            FileType typeVo=FileTypeCacheManager.getCacheValue(fileType);
            if(typeVo==null){
                typeVo=FileTypeCacheManager.getDefaultValue();
            }
            node.setIconCls(typeVo.getIconCls());
        }
        return node;
    }

    /**
     * <p>将文件转成base64 字符串</p>
     * @param path 文件路径
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path){
        File  file = new File(path);
        FileInputStream inputFile = null;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int)file.length()];
            inputFile.read(buffer);
            return new BASE64Encoder().encode(buffer);
        } catch (Exception e) {
            throw new PlatformException("将文件转成BASE64字符串异常",PlatformExceptionEnum.JE_CORE_UTIL_FILE_BASE64_ERROR,new Object[]{path},e);
        }finally {
            close(inputFile,null,null,null);
        }
    }

    /**
     * <p>将base64字符解码保存文件</p>
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void decoderBase64File(String base64Code,String targetPath){
        base64Code = base64Code.substring(base64Code.indexOf(",")+1);
        byte[] buffer = new byte[0];
        FileOutputStream out=null;
        try {
            buffer = new BASE64Decoder().decodeBuffer(base64Code);
            out = new FileOutputStream(targetPath);
            out.write(buffer);
            out.flush();
        } catch (Exception e) {
            throw new PlatformException("将base64字符解码保存文件异常",PlatformExceptionEnum.JE_CORE_UTIL_FILE_BASE64_ERROR,new Object[]{base64Code,targetPath},e);
        }finally {
            close(null,out,null,null);
        }
    }

    /**
     * <p>将base64字符保存文本文件</p>
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void toFile(String base64Code,String targetPath){
        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(targetPath);
            out.write(buffer);
        } catch (Exception e) {
            throw new PlatformException("将base64字符解码保存文件异常",PlatformExceptionEnum.JE_CORE_UTIL_FILE_BASE64_ERROR,new Object[]{base64Code,targetPath},e);
        }finally {
            close(null,out,null,null);
        }
    }

    /**
     * 截取图片
     * @param path
     * @param x
     * @param y
     * @param width
     * @param height
     * @return base64
     */
    public static String cutImage(String path,int x,int y, int width, int height){
        String imgStr = "";
        try {
            String imageType = path.substring(path.indexOf(".")+1,path.length());
            File input = new File(path);
            BufferedImage bi = ImageIO.read(input).getSubimage(x,y,width,height);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bi, imageType, os);
            imgStr = "data:image/png;base64,"+new BASE64Encoder().encode(os.toByteArray());
        }catch (IOException e) {
            // TODO Auto-generated catch block
           throw  new PlatformException("文件工具类截取图片异常",PlatformExceptionEnum.JE_CORE_UTIL_FILE_CUTIMG_ERROR,new Object[]{path,x,y,width,height},e);
        }
        return imgStr;
    }
}