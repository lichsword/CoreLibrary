package org.lichsword.android.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.lichsword.android.util.log.LogHelper;

import android.os.StatFs;
import android.text.TextUtils;

/**
 * FileSystemUtil of Snappy Food.
 * 
 * @author chzhong
 * 
 */
public final class FileSystemUtil {

    /**
     * Represent Unix Disk information.
     * 
     * @author chzhong
     * 
     */
    public static final class DiskInfo {
        /**
         * The total disk size of the disk, in bytes.
         */
        public final long totalSize;
        /**
         * The available disk size of the disk, in bytes.
         */
        public final long freeSize;
        /**
         * The Unix path of the disk.
         */
        public final String path;
        /**
         * The size, in bytes, of a block on the file system. This corresponds
         * to the Unix statfs.f_bsize field.
         */
        public final int blockSize;
        /**
         * The total number of blocks on the file system. This corresponds to
         * the Unix statfs.f_blocks field.
         */
        public final int totalBlockCount;
        /**
         * sThe number of blocks that are free on the file system and available
         * to applications. This corresponds to the Unix statfs.f_bavail field.
         */
        public final int freeBlockCount;

        DiskInfo(final String path) {
            this.path = path;
            final StatFs fs = new StatFs(path);
            final long blockSize = fs.getBlockSize();
            final long totalBlocks = fs.getBlockCount();
            final long freeBlocks = fs.getAvailableBlocks();

            totalSize = blockSize * totalBlocks;
            freeSize = blockSize * freeBlocks;
            this.blockSize = (int) blockSize;
            totalBlockCount = (int) totalBlocks;
            freeBlockCount = (int) freeBlocks;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return path;
        }

    }

    private static final String TAG = FileSystemUtil.class.getSimpleName();
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private FileSystemUtil() {
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
    public static void deleteDir(final File dir, final boolean removeDir,
            final FileFilter filter) {
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
    public static void deleteDir(final File dir, final boolean removeDir,
            final FilenameFilter filter) {
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

    public static boolean deleteFile(String fileAbsolutePath) {
        File file = new File(fileAbsolutePath);
        return file.delete();
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
        } catch (final FileNotFoundException e) {
            LogHelper.e(
                    TAG,
                    String.format("Can not open file %s for writing.",
                            file.toString()));
            e.printStackTrace();
        } catch (final IOException e) {
            LogHelper.e(
                    TAG,
                    String.format("Failed when writing data to file %s.",
                            file.toString()));
            e.printStackTrace();
        } finally {
            HttpClient.quietClose(fos);
        }
    }

    public static final void appContentToFile(final File file,
            final String content) {
        FileWriter writer;
        try {
            writer = new FileWriter(file, true);
            writer.write(content);
            writer.close();
        } catch (final IOException e) {
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
    public static final void writeDataToFile(final File file,
            final InputStream stream) {
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
        } catch (final FileNotFoundException e) {
            LogHelper.e(
                    TAG,
                    String.format("Can not open file %s for writing.",
                            file.toString()));
            e.printStackTrace();
        } catch (final IOException e) {
            LogHelper.e(
                    TAG,
                    String.format("Failed when writing data to file %s.",
                            file.toString()));
            e.printStackTrace();
        } finally {
            HttpClient.quietClose(fos);
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
    public static final void copyFile(final File srcFile, final File destFile) {
        if (null == srcFile) {
            throw new IllegalArgumentException("srcFile may not be null.");
        }
        if (null == destFile) {
            throw new IllegalArgumentException("destFile may not be null.");
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int bytesRead = fis.read(buffer);
            while (bytesRead > 0) {
                fos.write(buffer);
                bytesRead = fis.read(buffer);
            }
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            quietClose(fis);
            quietClose(fos);
        }
    }

    public static final void renameFile(final String srcFilePath,
            final String destFilePath) {
        moveFile(new File(srcFilePath), new File(destFilePath));
    }

    public static final void renameFile(final File srcFile, final File destFile) {
        moveFile(srcFile, destFile);
    }

    public static final void moveFile(final String srcFilePath,
            final String destFilePath) {
        moveFile(new File(srcFilePath), new File(destFilePath));
    }

    /**
     * Write the specified data to an specified file.
     * 
     * @param file
     *            The file to write into.
     * @param data
     *            The data to write. May be null.
     */
    public static final void moveFile(final File srcFile, final File destFile) {
        if (null == srcFile) {
            throw new IllegalArgumentException("srcFile may not be null.");
        }
        if (null == destFile) {
            throw new IllegalArgumentException("destFile may not be null.");
        }
        srcFile.renameTo(destFile);
    }

    /**
     * Close an {@linkplain Closeable} quietly.
     * 
     * @param closeable
     *            the {@linkplain Closeable} to close.
     */
    public static final void quietClose(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final IOException e) {
                // Ignore errors.
            }
        }
    }

    /**
     * Retrieve the total size of a disk.
     * 
     * @param diskPath
     *            the path of the disk.
     * @return the size of the disk in bytes.
     */
    public static long getDiskTotalSize(final String diskPath) {
        try {
            final StatFs stat = new StatFs(diskPath);
            final long blockSize = stat.getBlockSize();
            final long totalBlocks = stat.getBlockCount();
            final long totalSize = blockSize * totalBlocks;
            return totalSize;
        } catch (final Throwable e) {
            return 0;
        }
    }

    /**
     * Retrieve the free size of a disk.
     * 
     * @param diskPath
     *            the path of the disk.
     * @return the size of the disk in bytes.
     */
    public static long getDiskFreeSize(final String diskPath) {
        try {
            final StatFs stat = new StatFs(diskPath);
            final long blockSize = stat.getBlockSize();
            final long freeBlocks = stat.getAvailableBlocks();
            final long totalSize = blockSize * freeBlocks;
            return totalSize;
        } catch (final Throwable e) {
            return 0;
        }
    }

    /**
     * Retrieve disk information.
     * 
     * @return the disk information.
     */
    public static DiskInfo[] getDisks() {
        try {
            final Runtime runtime = Runtime.getRuntime();
            final Process process = runtime.exec("df");
            final InputStream stdout = process.getInputStream();
            final ArrayList<DiskInfo> disks = new ArrayList<DiskInfo>();
            if (stdout != null) {
                final BufferedReader reader = new BufferedReader(
                        new InputStreamReader(stdout));
                String line = reader.readLine();
                while (line != null) {
                    try {
                        final int pathEnd = line.indexOf(':');
                        final String path = line.substring(0, pathEnd);
                        disks.add(new DiskInfo(path));
                    } catch (final Exception e) {
                        LogHelper.e(TAG, String.format(
                                "Error parsing disk information : %s.", line));
                        e.printStackTrace();
                    }
                    line = reader.readLine();
                }
                stdout.close();
            }
            return disks.toArray(new DiskInfo[disks.size()]);
        } catch (final Exception e) {
            LogHelper.e(TAG, "Error getting disk information.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieve the main file name.
     * 
     * @param path
     *            the file name.
     * @return the main file name without the extension.
     */
    public static String getFileNameWithoutExtension(final String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return getFileNameWithoutExtension(new File(path));
    }

    /**
     * Retrieve the main file name.
     * 
     * @param file
     *            the file.
     * @return the main file name without the extension.
     */
    public static String getFileNameWithoutExtension(final File file) {
        if (null == file) {
            return null;
        }
        String fileName = file.getName();
        final int index = fileName.lastIndexOf('.');
        if (index >= 0) {
            fileName = fileName.substring(0, index);
        }
        return fileName;
    }

    /**
     * Retrieve the main file name.
     * 
     * @param path
     *            the file name.
     * @return the extension of the file.
     */
    public static String getExtension(final String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return getExtension(new File(path));
    }

    /**
     * Retrieve the extension of the file.
     * 
     * @param file
     *            the file.
     * @return the extension of the file.
     */
    public static String getExtension(final File file) {
        if (null == file) {
            return null;
        }
        final String fileName = file.getName();
        final int index = fileName.lastIndexOf('.');
        String extension;
        if (index >= 0) {
            extension = fileName.substring(index + 1);
        } else {
            extension = "";
        }
        return extension;
    }

    /**
     * compute the size of one folder
     * 
     * @param dir
     * @return the byte length for the folder
     */
    public static long computeFolderSize(final File dir) {
        if (dir == null) {
            return 0;
        }
        long dirSize = 0;
        final File[] files = dir.listFiles();
        if (null != files) {
            for (int i = 0; i < files.length; i++) {
                final File file = files[i];
                if (file.isFile()) {
                    dirSize += file.length();
                } else if (file.isDirectory()) {
                    dirSize += file.length();
                    dirSize += computeFolderSize(file);
                }
            }
        }
        return dirSize;
    }

    /**
     * <p>
     * 将文本文件全部读取为字符串
     * </p>
     * 
     * @param inputStream
     * @return
     */
    public static String readFileAsString(final InputStream inputStream) {
        final StringBuilder builder = new StringBuilder();
        if (null != inputStream) {
            final byte[] buffer = new byte[128];
            try {
                while (inputStream.read(buffer, 0, buffer.length) != -1) {
                    builder.append(new String(buffer, "utf-8"));
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

}
