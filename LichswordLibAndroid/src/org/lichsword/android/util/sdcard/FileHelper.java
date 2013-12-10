package org.lichsword.android.util.sdcard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author yuewang
 *
 */
public class FileHelper {
    /**
     * 默认的缓冲区大小
     */
    @SuppressWarnings("unused")
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private static final String LINE_DIVIDE = "\n";

    /**
     * 将File(一般为文本文件)转为BufferedReader接口，方便调用readLine()方法
     *
     * @param filePath
     * @return false if file is not exist or exception happened, otherwise
     *         return true.
     */
    public static synchronized BufferedReader readFileAsBufferedReader(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            try {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                bufferedReader = null;
            }
            return bufferedReader;
        } else {
            return null;
        }

    }

    /**
     *
     * @param filePath
     * @return null if File with 'filePath' is NOT exist.
     */
    public static synchronized String readFileFirstLineAsString(String filePath) {
        BufferedReader bufferedReader = readFileAsBufferedReader(filePath);

        if (null != bufferedReader) {
            StringBuilder sb = new StringBuilder();
            try {
                sb.append(bufferedReader.readLine());
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     *
     * @param filePath
     * @return null if File with 'filePath' is NOT exist.
     */
    public static synchronized String readFileAsString(String filePath) {
        BufferedReader bufferedReader = readFileAsBufferedReader(filePath);

        if (null != bufferedReader) {
            StringBuilder sb = new StringBuilder();
            String temp = null;
            try {
                do {
                    temp = bufferedReader.readLine();
                    if (null != temp) {
                        sb.append(temp);
                        sb.append(LINE_DIVIDE);
                    }// end if
                } while (null != temp);
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 通过指定正则参数，来过滤文件列表
     *
     * @param dir
     * @param patterns
     *            文件绝对路径的正则参数
     * @return
     */
    public static File[] listFiles(File dir, String[] patterns) {
        if (null != dir && dir.isDirectory()) {
            return dir.listFiles(new MyFileFilter(patterns));
        } else {
            return null;
        }
    }

    /**
     * 内部的文件正则表达式过滤类
     *
     * @author yuewang
     *
     */
    private static class MyFileFilter implements FileFilter {

        String[] patterns = null;

        public MyFileFilter(String[] patterns) {
            super();
            this.patterns = patterns;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.io.FileFilter#accept(java.io.File)
         */
        @Override
        public boolean accept(File file) {
            boolean accept = false;
            for (String pattern : patterns) {
                if (Pattern.matches(pattern, file.getAbsolutePath())) {
                    accept = true;
                    break;
                }
            }
            return accept;
        }
    }

    /**
     * 如果目标文件不存在，则方法内部会自动创建文件，并返回对应FileWriter
     *
     * @param filePath
     * @return
     */
    public static synchronized FileWriter getFileWriter(String filePath) {
        String path = filePath;
        if (null == path) {
            return null;
        }// end if

        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }// end if
            FileWriter fileWriter;
            fileWriter = new FileWriter(file);
            return fileWriter;
            // BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 如果目标文件不存在，则方法内部会自动创建文件，并返回对应BufferedWriter
     *
     * @param filePath
     * @param defaultFileName
     * @return
     */
    public static synchronized BufferedWriter getBufferedWriter(String filePath) {
        FileWriter fileWriter = getFileWriter(filePath);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        return bufferedWriter;
    }

    /**
     * 获取指定文件的FileInputStream
     *
     * @param filePath
     * @return null if target file is NOT exist.
     */
    public static synchronized FileInputStream getFileInputStream(String filePath) {
        FileInputStream result = null;

        File file = new File(filePath);
        if (file.exists()) {
            try {
                result = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                result = null;
                e.printStackTrace();
            }
        } else {
            result = null;
        }
        return result;
    }

    /**
     * 如果目标文件不存在，则方法内部会自动创建文件，并返回对应FileOutputStream
     *
     * @param filePath
     * @return null if target file is NOT exist.
     */
    public static synchronized FileOutputStream getFileOutputStream(String filePath) {
        FileOutputStream result = null;
        File file = new File(filePath);

        if (!file.exists()) {
            try {
                file.createNewFile();
                result = new FileOutputStream(file);
            } catch (IOException e) {
                result = null;
                e.printStackTrace();
            }
        }// end if
        return result;
    }
}
