package com.lichsword.stringtoclipboard;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    private static final String CTR_STRING = "-s";
    private static final String CTR_FILE = "-f";

    /**
     * @param args
     */
    public static void main(String[] args) {
        // args = new String[] { "-s", "hello, world" };
        // args = new String[] { "-f", "log.txt" };
        Main main = new Main();
        main.run(args);

    }

    public void run(String[] args) {

        int length = args.length;
        String control = null;
        String content = null;
        switch (length) {
        case 0:
            showHelp(HELP);
            break;
        case 1:
            control = args[0];
            if (control.equals(CTR_STRING)) {
                showHelp(HELP_OPTION_STING);
            } else if (control.equals(CTR_FILE)) {
                showHelp(HELP_OPTION_FILE);
            } else {
                showHelp(HELP);
            }
            break;
        case 2:
            control = args[0];
            content = args[1];
            if (control.equals(CTR_STRING)) {
                setSysClipboardText(content);
            } else if (control.equals(CTR_FILE)) {
                String path = content;
                if (!exist(path)) {
                    File file = new File(System.getProperty("user.dir"));
                    path = new File(file.getParent(), content).getAbsolutePath();
                }// end if
                content = readFile(new File(path));
                setSysClipboardText(content);
            }// end if
            break;
        default:
            break;
        }
        if (null != content && content.length() > 0) {
            System.out.println("has copy to system clipboard:\n" + content);
            content = null;
        } else {

        }
    }

    private static final int HELP = 0;
    private static final int HELP_OPTION_STING = 1;
    private static final int HELP_OPTION_FILE = 2;

    private static void showHelp(int index) {
        switch (index) {
        case HELP:
            System.out.println("Usage: java -jar StringToClipboard.jar [-options]");
            System.out.println();
            System.out.println("Where options include:");
            System.out.println("    -s<string>\t\trequire the target string to system clipboard.");
            System.out.println("    -f<filename>\trequire the string in target file to system clipboard.");
            System.out.println("    -? -help\t\tprint this help message");
            break;
        case HELP_OPTION_STING:
            System.out.println("Unrecognized option: -s");
            System.out.println("Could not copy string to clipboard.");
            break;
        case HELP_OPTION_FILE:
            System.out.println("Unrecognized option: -f");
            System.out.println("Could not copy string in file to clipboard.");
            break;
        default:
            break;
        }

    }

    /**
     * 从剪切板获得文字。
     */
    public static String getSysClipboardText() {
        String ret = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 获取剪切板中的内容
        Transferable clipTf = sysClip.getContents(null);

        if (clipTf != null) {
            // 检查内容是否是文本类型
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    ret = (String) clipTf.getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    /**
     * 将字符串复制到剪切板。
     */
    public static void setSysClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

    /**
     * 从剪切板获得图片。
     */
    @Deprecated
    public static Image getImageFromClipboard() throws Exception {
        Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable cc = sysc.getContents(null);
        if (cc == null)
            return null;
        else if (cc.isDataFlavorSupported(DataFlavor.imageFlavor))
            return (Image) cc.getTransferData(DataFlavor.imageFlavor);
        return null;
    }

    /**
     * 复制图片到剪切板。
     */
    @Deprecated
    public static void setClipboardImage(final Image image) {
        Transferable trans = new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[] { DataFlavor.imageFlavor };
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                if (isDataFlavorSupported(flavor))
                    return image;
                throw new UnsupportedFlavorException(flavor);
            }

        };
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
    }

    private static boolean exist(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    private static String readFile(final File file) {
        final StringBuffer builder = new StringBuffer();
        if (null != file && file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                final InputStreamReader fileReader = new InputStreamReader(fileInputStream);
                final BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                bufferedReader.close();
                fileReader.close();
                fileInputStream.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
