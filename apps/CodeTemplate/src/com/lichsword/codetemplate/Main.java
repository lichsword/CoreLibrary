package com.lichsword.codetemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * <p>
 * 注意
 * </p>
 * <p>
 * 因为使用了资源目录，所以打包时要先Jar file, 而不是Runnable Jar file，选中src/ 和/a目录，其它工程配置文件不需要。
 * </p>
 * 
 * @author lichsword
 * 
 */
public class Main {

    private static final String CTR_LIST = "-l";
    private static final String CTR_KEYWORD = "-k";

    private static final int HELP = 0;
    private static final int HELP_OPTION_KEYWORD = 1;
    private static final int HELP_OPTION_LIST = 2;

    /**
     * <code>
public class ClassName {

    private static ClassName sInstance = null;

    public static ClassName getInstance() {
       if (null == sInstance) {
            synchronized (ClassName.class) {
                if (null == sInstance) {
                    sInstance = new ClassName();
                }
            }
        }
        return sInstance;
    }

}
     * </code>
     */
    @SuppressWarnings("unused")
    private static final String sTemplateSingleton = "public class ClassName {\n\n    private static ClassName sInstance = null;\n\n    public static ClassName getInstance() {\n       if (null == sInstance) {\n            synchronized (ClassName.class) {\n                if (null == sInstance) {\n                    sInstance = new ClassName();\n                }\n            }\n        }\n        return sInstance;\n    }\n\n}";

    /**
     * @param args
     */
    public static void main(String[] args) {
        // args = new String[] { "-l" };
        Main main = new Main();
        main.run(args);
    }

    public void run(String[] args) {
        // args = new String[] { "-k", "Application" };

        int length = args.length;
        String control = null;
        String content = null;
        switch (length) {
        case 0:
            showHelp(HELP);
            break;
        case 1:
            control = args[0];
            if (control.equals(CTR_LIST)) {
                showHelp(HELP_OPTION_LIST);
            } else if (control.equals(CTR_KEYWORD)) {
                showHelp(HELP_OPTION_KEYWORD);
            } else {
                showHelp(HELP);
            }
            break;
        case 2:
            control = args[0];
            content = args[1];
            if (control.equals(CTR_KEYWORD)) {
                try {
                    String code = generalCode(content);
                    System.out.println(code);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showHelp(HELP);
            }
            break;
        default:
            break;
        }
    }

    /**
     * 生成代码
     * 
     * @param content
     * @return
     * @throws IOException
     */
    private String generalCode(String keyword) throws IOException {
        String result = null;
        try {
            // result = sTemplateSingleton;
            URL url = this.getClass().getResource("/Templates/singleton");
            if (null != url) {
                result = readInputStream(url.openStream());
            }// end if
            if (null != result) {
                result = result.replace("ClassName", keyword);
            }// end if
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    private void showHelp(int index) {
        switch (index) {
        case HELP:
            System.out.println("Usage: java -jar CodeTemplate.jar [-options]");
            System.out.println();
            System.out.println("Where options include:");
            System.out.println("    -l\tlist supported code templates.");
            System.out.println("    -k<string>\t\trequire the keyword of code template.");
            System.out.println("    -? -help\t\tprint this help message");
            break;
        case HELP_OPTION_KEYWORD:
            System.out.println("Unrecognized option: -k");
            System.out.println("Could not execute code template for miss keyword.");
            break;
        case HELP_OPTION_LIST:
            System.out.println("Code template list: (1) count");
            System.out.println("1. Singleton.");
            break;
        default:
            break;
        }

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
