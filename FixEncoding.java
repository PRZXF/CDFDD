import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

/**
 * 编码修复工具类
 * <p>
 * 负责修复Java源文件的编码问题
 * 确保文件使用UTF-8编码，去除BOM标记
 * </p>
 *
 * @author 系统开发团队
 * @version 1.0
 */
public class FixEncoding {
    public static void main(String[] args) {
        String[] files = {
                "src\\com\\office\\shopping\\controller\\LoginPanel.java",
                "src\\com\\office\\shopping\\controller\\MainPanel.java",
                "src\\com\\office\\shopping\\controller\\CartPanel.java",
                "src\\com\\office\\shopping\\controller\\OrdersPanel.java"
        };

        for (String file : files) {
            try {
                fixFileEncoding(file);
                System.out.println("Fixed: " + file);
            } catch (IOException e) {
                System.err.println("Error fixing " + file + ": " + e.getMessage());
            }
        }
    }

    private static void fixFileEncoding(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));

        int startIndex = 0;
        if (bytes.length >= 3 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
            startIndex = 3;
        }

        String content = new String(bytes, startIndex, bytes.length - startIndex, StandardCharsets.UTF_8);

        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8)) {
            writer.write(content);
        }
    }
}
