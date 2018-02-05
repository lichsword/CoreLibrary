package org.lichsword.android.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.StatFs;

public class DiskUtil {

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

            this.totalSize = blockSize * totalBlocks;
            this.freeSize = blockSize * freeBlocks;
            this.blockSize = (int) blockSize;
            this.totalBlockCount = (int) totalBlocks;
            this.freeBlockCount = (int) freeBlocks;
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

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private DiskUtil() {
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
                final BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
                String line = reader.readLine();
                while (line != null) {
                    try {
                        final int pathEnd = line.indexOf(':');
                        final String path = line.substring(0, pathEnd);
                        disks.add(new DiskInfo(path));
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                    line = reader.readLine();
                }
                stdout.close();
            }
            return disks.toArray(new DiskInfo[disks.size()]);
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
