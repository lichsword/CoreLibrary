package com.lichsword.stringtoclipboard;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Main {

    private static final String CTR_STRING = "-s";
    private static final String CTR_FILE = "-f";

    /**
     * @param args
     */
    public static void main(String[] args) {
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
            if (control.equals(CTR_STRING) || control.equals(CTR_FILE)) {
                setSysClipboardText(content);
            } else {
                // TODO
            }
            break;
        default:
            break;
        }
        if (null != content && content.length() > 0) {
            System.out.println("has copy to system clipboard:\n\t" + content);
            content = null;
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

}
