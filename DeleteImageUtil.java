
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteImageUtil {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\L Z\\Desktop\\MyBlog\\source\\_posts");

        File[] files = file.listFiles();
        /*所有文件夹*/
        List<String> dirPathList = Arrays.stream(files).filter(e -> e.isDirectory()).map(e -> e.getPath()).collect(Collectors.toList());

        /*所有文件*/
        List<String> filePathList = Arrays.stream(files).filter(e -> e.isFile()).map(e -> e.getPath()).collect(Collectors.toList());

        for (String dirPath : dirPathList) {
            File dirFile = new File(dirPath);
            File[] imageList = dirFile.listFiles();

            for (File image : imageList) {
                int notHaveFlag = 0;
                for (int i = 1; i <= 2; i++) {
                    String name = image.getName();
                    if (i == 2) {
                        name = URLEncoder.encode(name, "UTF-8").replace("+", "%20");
                    }

                    boolean flag = false;

                    for (String notePath : filePathList) {
                        FileInputStream inputStream = new FileInputStream(notePath);
                        byte[] b = new byte[inputStream.available()];

                        int read = inputStream.read(b);

                        String s = new String(b);
                        if (s.contains(name)) {
                            flag = true;
                            break;
                        } else {
                            notHaveFlag++;
                        }
                    }

                    if (!flag && notHaveFlag == filePathList.size()*2) {
                        File bkFile = new File("C:\\Users\\L Z\\Desktop\\imagesBk\\" + dirFile.getName());

                        if (!bkFile.exists()) {
                            bkFile.mkdirs();
                        }

                        Path srcPath = Paths.get(image.getPath());
                        Path tarPath = Paths.get("C:\\Users\\L Z\\Desktop\\imagesBk\\" + dirFile.getName() + "\\" + URLDecoder.decode(name, "UTF-8"));

                        Files.move(srcPath, tarPath);
                    }
                }
            }
        }
    }
}
