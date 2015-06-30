import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.firefoxmmx.ocr.ImageFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
  private static int[] WHITE = new int[]{255, 255, 255};
  private static int[] BLACK = new int[]{0, 0, 0};
  private static int[] GRAY = new int[]{55,55,55};
  private static int[] ID_GRAY = new int[]{30,30,30};

  public static void main(String[] args) throws TesseractException, IOException {
    File imageFile = new File("/home/hooxin/Downloads/IDCARD1/0.tif");


    Tesseract tesseract = new Tesseract();
    tesseract.setDatapath("/usr/share/tessdata/");
    tesseract.setLanguage("chi_sim");
    BufferedImage bufferedImage = ImageFilter.cloneImage(ImageIO.read(imageFile));
    bufferedImage = ImageFilter.convertImageToGrayScale(bufferedImage);
    //缩放到真实身份证大小
    bufferedImage = ImageFilter.imageScale(bufferedImage, 673, 425);
    ImageIO.write(bufferedImage, "jpg", new FileOutputStream(imageFile.getParent() + "/1.jpg"));
    int limit=25;
    BufferedImage contentImage = ImageFilter.subImage(bufferedImage, bufferedImage.getMinX(), bufferedImage.getMinY(), 414, 154);
    System.out.println("contentImage Brightness = "+ImageFilter.imageBrightness(contentImage));
    ImageFilter.convertToBinary(contentImage, GRAY);
    BufferedImage birthImage=ImageFilter.subImage(bufferedImage, bufferedImage.getMinX(), 154, 414, 54);
    System.out.println("birthImage Brightness = "+ ImageFilter.imageBrightness(birthImage));
    ImageFilter.convertToBinary(birthImage, GRAY);
    BufferedImage addressImage = ImageFilter.subImage(bufferedImage, bufferedImage.getMinX(), 208, 414, 144);
    System.out.println("addressImage Brightness = "+ ImageFilter.imageBrightness(addressImage));
    ImageFilter.convertToBinary(addressImage, GRAY);

    BufferedImage idImage = ImageFilter.subImage(bufferedImage, bufferedImage.getMinX(), 354, bufferedImage.getWidth(), bufferedImage.getHeight() - 354);
    System.out.println("idImage Brightness = "+ ImageFilter.imageBrightness(idImage));
    ImageFilter.convertToBinary(idImage, ID_GRAY);
    ImageIO.write(contentImage, "jpg", new FileOutputStream(imageFile.getParent() + "/"  + "contentImage.jpg"));
    String result = tesseract.doOCR(contentImage);
    System.out.println(result);
    ImageIO.write(birthImage,"jpg",new FileOutputStream(imageFile.getParent()+"/"+"birthImage.jpg"));
    tesseract.setLanguage("eng");
    System.out.println(tesseract.doOCR(birthImage));
    ImageIO.write(addressImage,"jpg",new FileOutputStream(imageFile.getParent()+"/"+"addressImage.jpg"));
    tesseract.setLanguage("chi_sim");
    System.out.println(tesseract.doOCR(addressImage));
    ImageIO.write(idImage,"jpg",new FileOutputStream(imageFile.getParent()+"/"+"idImage.jpg"));
    tesseract.setLanguage("eng");
    System.out.println(tesseract.doOCR(idImage));
  }


}
