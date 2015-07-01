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
  private static int[] GRAY = new int[]{65, 65, 65};
  private static int[] BIRTH_GRAY = new int[]{73, 73, 73};
  private static int[] ID_GRAY = new int[]{30, 30, 30};
  private static int targetContentBrightness = 143;
  private static int targetBirthBrightness = 130;
  private static int targetIdBrightness = 91;
  private static int targetAddressBrightness = 110;
  private static int targetDifferenceValue=10;

  public static void main(String[] args) throws TesseractException, IOException {
    File imageFile = new File("/home/hooxin/Downloads/IDCARD2/0.tif");


    Tesseract tesseract = new Tesseract();
    tesseract.setDatapath("/usr/share/tessdata/");
    tesseract.setLanguage("chi_sim");
    BufferedImage bufferedImage = ImageFilter.cloneImage(ImageIO.read(imageFile));
    bufferedImage=ImageFilter.imageRGBDifferenceFilter(bufferedImage, targetDifferenceValue);
    ImageIO.write(bufferedImage, "jpg", new FileOutputStream(imageFile.getParent() + "/0.1.jpg"));
    bufferedImage = ImageFilter.convertImageToGrayScale(bufferedImage);
    //缩放到真实身份证大小
    bufferedImage = ImageFilter.imageScale(bufferedImage, 673, 425);
    ImageIO.write(bufferedImage, "jpg", new FileOutputStream(imageFile.getParent() + "/1.jpg"));
    BufferedImage contentImage = ImageFilter.subImage(bufferedImage, bufferedImage.getMinX(), bufferedImage.getMinY(), 414, 154);
    int contentBrightness = ImageFilter.imageBrightness(contentImage);
    System.out.println("contentImage Brightness = " + ImageFilter.imageBrightness(contentImage));
    int fixedBrightness = targetContentBrightness - contentBrightness;
    if (fixedBrightness != 0)
      contentImage = ImageFilter.imageBrightness(contentImage, fixedBrightness);
    System.out.println("contentImage after Brightness = " + ImageFilter.imageBrightness(contentImage));
    ImageIO.write(contentImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "contentImageBefore.jpg"));
    ImageFilter.convertToBinary(contentImage, GRAY);
    BufferedImage birthImage = ImageFilter.subImage(bufferedImage, bufferedImage.getMinX(), 154, 414, 54);
    int birthBrightness = ImageFilter.imageBrightness(birthImage);
    System.out.println("birthImage Brightness = " + birthBrightness);
    fixedBrightness = targetBirthBrightness - birthBrightness;
    if (fixedBrightness != 0)
      birthImage = ImageFilter.imageBrightness(birthImage, fixedBrightness);
    System.out.println("birthImage after brightness = " + ImageFilter.imageBrightness(birthImage));
    ImageIO.write(birthImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "birthImageBefore.jpg"));
    ImageFilter.convertToBinary(birthImage, BIRTH_GRAY);
    BufferedImage addressImage = ImageFilter.subImage(bufferedImage, bufferedImage.getMinX(), 208, 414, 144);
    int addressBrightness = ImageFilter.imageBrightness(addressImage);
    System.out.println("addressImage Brightness = " + addressBrightness);
    fixedBrightness = targetAddressBrightness - addressBrightness;
    if (fixedBrightness != 0)
      addressImage = ImageFilter.imageBrightness(addressImage, fixedBrightness);
    System.out.println("addressImage after Brightness = " + ImageFilter.imageBrightness(addressImage));
    ImageIO.write(addressImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "addressImageBefore.jpg"));
    ImageFilter.convertToBinary(addressImage, GRAY);

    BufferedImage idImage = ImageFilter.subImage(bufferedImage, bufferedImage.getMinX(), 354, bufferedImage.getWidth(), bufferedImage.getHeight() - 354);
    int idBrightness = ImageFilter.imageBrightness(idImage);
    fixedBrightness = targetIdBrightness - idBrightness;
    if (fixedBrightness != 0)
      idImage = ImageFilter.imageBrightness(idImage, fixedBrightness);
    System.out.println("idImage Brightness = " + ImageFilter.imageBrightness(idImage));
    ImageIO.write(idImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "idImageBefore.jpg"));
    ImageFilter.convertToBinary(idImage, ID_GRAY);
    ImageIO.write(contentImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "contentImage.jpg"));
    String result = tesseract.doOCR(contentImage);
    System.out.println(result);
    ImageIO.write(birthImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "birthImage.jpg"));
    tesseract.setLanguage("eng");
    System.out.println(tesseract.doOCR(birthImage));
    ImageIO.write(addressImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "addressImage.jpg"));
    tesseract.setLanguage("chi_sim");
    System.out.println(tesseract.doOCR(addressImage));
    ImageIO.write(idImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "idImage.jpg"));
    tesseract.setLanguage("eng");
    System.out.println(tesseract.doOCR(idImage));
  }


}
