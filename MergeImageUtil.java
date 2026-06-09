import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MergeImageUtil {
    public static void main(String[] args) throws IOException {
        // 源目录路径
        Path path01 = Paths.get("G:\\L ZHEN\\Desktop\\_posts");
        String path02 = "G:\\L ZHEN\\Desktop\\imagesBk\\";
        String targetPath = "G:\\L ZHEN\\Desktop\\_posts\\";

        /*获取1中所有子目录*/
        List<Path> dirList01 = Files.list(path01).collect(Collectors.toList());

        ArrayList<Path> sameImage  = new ArrayList<>();
        for (Path subDirPath01 : dirList01) {
            /*获取1中子目录的名称*/
            Path fileName = subDirPath01.getFileName();

            /*创建目标子文件夹*/
            Path path = Paths.get(targetPath + fileName);
            Files.createDirectories(path);

            /*获取2中对应1的子目录*/
            Path subDirPath02 = Paths.get(path02 + fileName);
            boolean exists = Files.exists(subDirPath02);

            /*获取1中子目录中所有的图片*/
            List<Path> imagePath01 = Files.list(subDirPath01).collect(Collectors.toList());

            List<Path> imagePath02 = null;
            if (exists) {
                imagePath02 = Files.list(subDirPath02).collect(Collectors.toList());
            }
            for (Path image01 : imagePath01) {
                Path imageName = image01.getFileName();
                /*删除2中相同的图片*/
                if (exists) {
                    Path image02 = Paths.get(path02 + fileName + "\\" + imageName);
                    if (Files.exists(image02)) {
                        sameImage.add(image02);
                        imagePath02.remove(image02);
                        Files.delete(image02);

                    }
                }
                /*将1中图片移动到目标子文件夹*/
                Files.move(image01, Paths.get(targetPath + fileName + "\\" + imageName));
            }

            /*判断2中有的，但是1中没有*/
            if (imagePath02 != null && imagePath02.size() > 0) {
                for (Path path1 : imagePath02) {
                    Path fileName1 = path1.getFileName();
                    /*判断目标文件夹中是否有2中子目录*/
                    boolean exists1 = Files.exists(Paths.get(targetPath + fileName));
                    if (!exists1) {
                        Files.createDirectories(Paths.get(targetPath + fileName));
                    }
                    Path file = Paths.get(targetPath + fileName + "\\" + fileName1);
                    boolean exists2 = Files.exists(file);
                    if (!exists2) {
                        Files.move(path1, file);
                    }
                }
            }
        }
        System.out.println("sameImage.toString() = " + sameImage.toString());
        System.out.println("sameImage.size() = " + sameImage.size());

        List<Path> dirList02 = Files.list(Paths.get(path02)).collect(Collectors.toList());
        for (Path dir : dirList02) {
            List<Path> images = Files.list(dir).collect(Collectors.toList());
            if (images != null && images.size() > 0) {
                for (Path image : images) {
                    Path fileName = dir.getFileName();
                    if (!Files.exists(Paths.get(targetPath + fileName))) {
                        Files.createDirectories(Paths.get(targetPath + fileName));
                    }

                    Path imageFileName = image.getFileName();
                    if (!Files.exists(Paths.get(targetPath + fileName + "\\" + imageFileName))) {
                        Files.move(image, Paths.get(targetPath + fileName + "\\" + imageFileName));
                    }
                }
            }
        }
    }
}
