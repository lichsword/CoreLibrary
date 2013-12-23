package org.lichsword.java.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Java 文件操作工具类
 * 
 * @author wangyue.wy
 * 
 */
public class FileUtil {

    public static final String TAG = FileUtil.class.getSimpleName();

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public static void copy(File src, File dst) throws IOException {
        BufferedInputStream bIn = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream bOut = new BufferedOutputStream(new FileOutputStream(dst));

        byte[] buf = new byte[1024];
        int len;
        while ((len = bIn.read(buf)) > 0) {
            bOut.write(buf, 0, len);
        }
        bIn.close();
        bOut.close();
    }

    /**
     * ensure a dir exist.
     * 
     * @param dirPath
     * @return
     */
    public static boolean ensureDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        }// end if
        return true;
    }

    /**
     * check file exist.
     * 
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * If file has exist & is not directory, delete it.
     * 
     * @param path
     *            the file to delete
     * @return true if file deleted.
     */
    public static boolean deleteFile(String path) {
        boolean success = false;
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            file.delete();
            success = true;
        } else {
            success = false;
        }
        return success;
    }

    /**
     * Clean a specified directory.
     * 
     * @param dir
     *            the directory to clean.
     */
    public static void cleanDir(final File dir) {
        deleteDir(dir, false);
    }

    /**
     * Clean a specified directory.
     * 
     * @param dir
     *            the directory to clean.
     * @param filter
     *            the filter to determine which file or directory to delete.
     */
    public static void cleanDir(final File dir, final FilenameFilter filter) {
        deleteDir(dir, false, filter);
    }

    /**
     * Clean a specified directory.
     * 
     * @param dir
     *            the directory to clean.
     * @param filter
     *            the filter to determine which file or directory to delete.
     */
    public static void cleanDir(final File dir, final FileFilter filter) {
        deleteDir(dir, false, filter);
    }

    /**
     * Clean dir but ignore file in parameter ignoreFilePathList.
     * 
     * @param dir
     * @param ignoreFilePathList
     */
    public static void cleanDir(File dir, ArrayList<String> ignoreFilePathList) {
        File[] fileList = null;
        if (dir.exists() && dir.isDirectory()) {
            fileList = dir.listFiles();
            for (File file : fileList) {
                if (ignoreFilePathList.contains(file.getAbsolutePath())) {
                    // do nothing
                    continue;
                }// end if

                if (file.isFile()) {
                    file.delete();
                }// end if
            }
        }// end if
    }

    /**
     * Delete a specified directory.
     * 
     * @param dir
     *            the directory to clean.
     */
    public static void deleteDir(final File dir) {
        deleteDir(dir, true);
    }

    /**
     * Delete a specified directory.
     * 
     * @param dir
     *            the directory to clean.
     * @param filter
     *            the filter to determine which file or directory to delete.
     */
    public static void deleteDir(final File dir, final FileFilter filter) {
        deleteDir(dir, true, filter);
    }

    /**
     * Delete a specified directory.
     * 
     * @param dir
     *            the directory to clean.
     * @param filter
     *            the filter to determine which file or directory to delete.
     */
    public static void deleteDir(final File dir, final FilenameFilter filter) {
        deleteDir(dir, true, filter);
    }

    /**
     * Delete a specified directory.
     * 
     * @param dir
     *            the directory to clean.
     * @param removeDir
     *            true to remove the {@code dir}.
     */
    public static void deleteDir(final File dir, final boolean removeDir) {
        if (dir != null && dir.isDirectory()) {
            final File[] files = dir.listFiles();
            for (final File file : files) {
                if (file.isDirectory()) {
                    deleteDir(file, removeDir);
                } else {
                    file.delete();
                }
            }
            if (removeDir) {
                dir.delete();
            }
        }
    }

    /**
     * Delete a specified directory.
     * 
     * @param dir
     *            the directory to clean.
     * @param removeDir
     *            true to remove the {@code dir}.
     * @param filter
     *            the filter to determine which file or directory to delete.
     */
    public static void deleteDir(final File dir, final boolean removeDir, final FileFilter filter) {
        if (dir != null && dir.isDirectory()) {
            final File[] files = dir.listFiles(filter);
            if (files != null) {
                for (final File file : files) {
                    if (file.isDirectory()) {
                        deleteDir(file, removeDir, filter);
                    } else {
                        file.delete();
                    }
                }
            }
            if (removeDir) {
                dir.delete();
            }
        }
    }

    /**
     * Delete a specified directory.
     * 
     * @param dir
     *            the directory to clean.
     * @param removeDir
     *            true to remove the {@code dir}.
     * @param filter
     *            the filter to determine which file or directory to delete.
     */
    public static void deleteDir(final File dir, final boolean removeDir, final FilenameFilter filter) {
        if (dir != null && dir.isDirectory()) {
            final File[] files = dir.listFiles(filter);
            if (files != null) {
                for (final File file : files) {
                    if (file.isDirectory()) {
                        deleteDir(file, removeDir, filter);
                    } else {
                        file.delete();
                    }
                }
            }
            if (removeDir) {
                dir.delete();
            }
        }
    }

    /**
     * Write the specified data to an specified file.
     * 
     * @param file
     *            The file to write into.
     * @param data
     *            The data to write. May be null.
     */
    public static final void writeDataToFile(final File file, byte[] data) {
        if (null == file) {
            throw new IllegalArgumentException("file may not be null.");
        }
        if (null == data) {
            data = new byte[0];
        }
        final File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the specified data to an specified file.
     * 
     * @param file
     *            The file to write into.
     * @param stream
     *            The stream to write.
     */
    public static final void writeDataToFile(final File file, final InputStream stream) {
        if (null == file) {
            throw new IllegalArgumentException("file may not be null.");
        }
        if (null == stream) {
            throw new IllegalArgumentException("stream may not be null.");
        }
        final File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream fos = null;
        final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        try {
            fos = new FileOutputStream(file);
            int bytesRead = stream.read(buffer);
            while (bytesRead > 0) {
                fos.write(buffer, 0, bytesRead);
                bytesRead = stream.read(buffer);
            }
            fos.close();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFileNameWithExtension(String filePath) {
        if (null == filePath || filePath.length() <= 0) {
            return null;
        }
        String[] pathes = filePath.split(File.separator);
        if (pathes.length > 0) {
            return pathes[pathes.length - 1];

        } else {
            return filePath;
        }
    }

    public static String getFileNameWithoutExtension(String filePath) {
        String fileNameWithExt = getFileNameWithExtension(filePath);
        if (null != fileNameWithExt && fileNameWithExt.length() > 0) {
            String[] subNames = fileNameWithExt.split("\\.");
            if (subNames.length > 0) {
                StringBuilder sb = new StringBuilder(subNames[0]);
                for (int i = 1; i < subNames.length - 1; i++) {
                    sb.append(subNames[i]);
                }
                return sb.toString();
            } else {
                return fileNameWithExt;
            }
        } else {
            return null;
        }
    }

    public static String readFile(String filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    public static String readInputStream(InputStream inputStream) {
        final StringBuilder builder = new StringBuilder();
        if (null != inputStream) {
            try {
                InputStreamReader reader = new InputStreamReader(inputStream);
                final BufferedReader bufferedReader = new BufferedReader(reader);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                bufferedReader.close();
                reader.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
