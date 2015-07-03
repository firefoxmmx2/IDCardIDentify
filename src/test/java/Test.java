import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import org.firefoxmmx.ocr.ImageFilter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
  private static int[] WHITE = new int[]{255, 255, 255};
  private static int[] BLACK = new int[]{0, 0, 0};
  private static int[] GRAY = new int[]{245, 245, 245};
  private static int[] BIRTH_GRAY = new int[]{73, 73, 73};
  private static int[] ID_GRAY = new int[]{100, 100, 100};
  private static int targetContentBrightness = 300;
  private static int targetBirthBrightness = 300;
  private static int targetIdBrightness = 300;
  private static int targetAddressBrightness = 300;
  private static int targetDifferenceValue=15;

  public static void main(String[] args) throws TesseractException, IOException {
    File imageFile = new File("/home/hooxin/Downloads/IDCARD1/0.tif");


    Tesseract tesseract = new Tesseract();
    tesseract.setDatapath("/usr/share/tessdata/");
    tesseract.setLanguage("chi_sim");
    BufferedImage bufferedImage = ImageFilter.cloneImage(ImageIO.read(imageFile));
    bufferedImage = ImageFilter.imageRGBDifferenceFilter(bufferedImage, targetDifferenceValue);
    bufferedImage = ImageFilter.convertImageToGrayScale(bufferedImage);
    //缩放到真实身份证大小
    bufferedImage = ImageFilter.imageScale(bufferedImage, 673, 425);
//    ImageFilter.convertToWhite(bufferedImage);
    ImageIO.write(bufferedImage, "jpg", new FileOutputStream(imageFile.getParent() + "/1.jpg"));
    BufferedImage contentImage = ImageFilter.subImage(bufferedImage, bufferedImage.getMinX(), bufferedImage.getMinY(), 414, 154);
    int contentBrightness = ImageFilter.imageBrightness(contentImage);
    System.out.println("contentImage Brightness = " + ImageFilter.imageBrightness(contentImage));
    int fixedBrightness = targetContentBrightness - contentBrightness;
    if (fixedBrightness != 0)
      contentImage = ImageFilter.imageBrightness(contentImage, fixedBrightness);
    System.out.println("contentImage after Brightness = " + ImageFilter.imageBrightness(contentImage));
    ImageIO.write(contentImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "contentImageBefore.jpg"));
//    contentImage = ImageHelper.convertImageToBinary(contentImage);
//    ImageFilter.convertToBinary(contentImage,GRAY);
//    ImageFilter.removeBrinaryImageNoisePoint(contentImage);
    BufferedImage birthImage = ImageFilter.subImage(bufferedImage, bufferedImage.getMinX(), 154, 414, 54);
    int birthBrightness = ImageFilter.imageBrightness(birthImage);
    System.out.println("birthImage Brightness = " + birthBrightness);
    fixedBrightness = targetBirthBrightness - birthBrightness;
    if (fixedBrightness != 0)
      birthImage = ImageFilter.imageBrightness(birthImage, fixedBrightness);
    System.out.println("birthImage after brightness = " + ImageFilter.imageBrightness(birthImage));
    ImageIO.write(birthImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "birthImageBefore.jpg"));
//    ImageFilter.convertToBinary(birthImage, BIRTH_GRAY);
//    ImageFilter.convertToBinary(birthImage, GRAY);
//    ImageFilter.removeBrinaryImageNoisePoint(birthImage);
    BufferedImage addressImage = ImageFilter.subImage(bufferedImage, bufferedImage.getMinX(), 208, 414, 144);
    int addressBrightness = ImageFilter.imageBrightness(addressImage);
    System.out.println("addressImage Brightness = " + addressBrightness);
    fixedBrightness = targetAddressBrightness - addressBrightness;
    if (fixedBrightness != 0)
      addressImage = ImageFilter.imageBrightness(addressImage, fixedBrightness);
    System.out.println("addressImage after Brightness = " + ImageFilter.imageBrightness(addressImage));
    ImageIO.write(addressImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "addressImageBefore.jpg"));
//    ImageFilter.convertToBinary(addressImage, GRAY);
//    ImageFilter.removeBrinaryImageNoisePoint(addressImage);
    BufferedImage idImage = ImageFilter.subImage(bufferedImage, bufferedImage.getMinX(), 354, bufferedImage.getWidth(), bufferedImage.getHeight() - 354);
    int idBrightness = ImageFilter.imageBrightness(idImage);
    fixedBrightness = targetIdBrightness - idBrightness;
    if (fixedBrightness != 0)
      idImage = ImageFilter.imageBrightness(idImage, fixedBrightness);
    System.out.println("idImage Brightness = " + ImageFilter.imageBrightness(idImage));
    ImageIO.write(idImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "idImageBefore.jpg"));
//    ImageFilter.convertToBinary(idImage, GRAY);
//    ImageFilter.removeBrinaryImageNoisePoint(idImage);
    ImageIO.write(contentImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "contentImage.jpg"));
    String result = tesseract.doOCR(contentImage);
    System.out.println(result);
    String[] resultArray=result.split("\n");
    String name = resultArray[0].replaceAll("[^\\u4e00-\\u9fa5]", "").trim();
    System.out.println(name);
    String sex,nation;
    String[] sexAbout=resultArray[1].replaceAll("[^\\u4e00-\\u9fa5 ]","").trim().split("\\s+");
    sex=sexAbout[0];
    nation=sexAbout[1];
    System.out.println(sex);
    System.out.println(nation);
    ImageIO.write(birthImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "birthImage.jpg"));
    tesseract.setLanguage("eng");
    System.out.println(tesseract.doOCR(birthImage));
    ImageIO.write(addressImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "addressImage.jpg"));
    tesseract.setLanguage("chi_sim");
    System.out.println(tesseract.doOCR(addressImage).replaceAll("\\s+",""));
    ImageIO.write(idImage, "jpg", new FileOutputStream(imageFile.getParent() + "/" + "idImage.jpg"));
    tesseract.setLanguage("eng");
    System.out.println(tesseract.doOCR(idImage).replaceAll("[^0-9xX]",""));

  }


}
