import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import net.sourceforge.tess4j.util.ImageIOHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
  private static int[] WHITE = new int[]{255, 255, 255};
  private static int[] BLACK = new int[]{0, 0, 0};
  private static int[] GRAY = new int[]{60,60,60};
  public static Object darkestPoint(BufferedImage image) {
    Object darkestPoint = image.getRaster().getDataElements(image.getMinX(),image.getMinY(),null);
    for (int x = image.getMinX(); x < image.getWidth(); x++) {
      for (int y = image.getMinY(); y < image.getHeight(); y++) {
        Object data = image.getRaster().getDataElements(x,y,null);
        int dataBlue=image.getColorModel().getBlue(data);
        int dataRed=image.getColorModel().getRed(data);
        int dataGreen=image.getColorModel().getGreen(data);

        if(dataBlue < image.getColorModel().getBlue(darkestPoint) &&
            dataRed < image.getColorModel().getRed(darkestPoint) &&
            dataGreen < image.getColorModel().getGreen(darkestPoint))
          darkestPoint = data;
      }
    }

    return darkestPoint;
  }

  public static void convertToBinary(BufferedImage image,int[] rgb ){
    for (int x = image.getMinX(); x < image.getWidth(); x++) {
      for (int y = image.getMinY(); y < image.getHeight(); y++) {
        Object data = image.getRaster().getDataElements(x,y,null);
        int blue= image.getColorModel().getBlue(data);
        int red = image.getColorModel().getRed(data);
        int green = image.getColorModel().getGreen(data);

        if(red > rgb[0] && green > rgb[1] && blue > rgb[2])
          image.getRaster().setPixel(x,y,WHITE);
        else
          image.getRaster().setPixel(x,y,BLACK);
      }
    }
  }
  public static void main(String[] args) throws TesseractException, IOException {
    File imageFile = new File("/home/hooxin/Downloads/IDCARD1/0.tif");


    Tesseract tesseract = new Tesseract();
    tesseract.setDatapath("/usr/share/tessdata/");
    tesseract.setLanguage("chi_sim");
    BufferedImage bufferedImage = ImageHelper.cloneImage(ImageIO.read(imageFile));
    bufferedImage = ImageHelper.convertImageToGrayscale(bufferedImage);
    //缩放到真实身份证大小
    bufferedImage = ImageHelper.getScaledInstance(bufferedImage, 673, 425);
    ImageIO.write(bufferedImage, "jpg", new FileOutputStream(imageFile.getParent() + "/1.jpg"));
    int limit=15;
    BufferedImage contentImage = ImageHelper.getSubImage(bufferedImage,bufferedImage.getMinX(),bufferedImage.getMinY(),414,154);
    Object darkestPoint = darkestPoint(contentImage);
    int[] referenceColor=new int[]{
        contentImage.getColorModel().getRed(darkestPoint),
        contentImage.getColorModel().getGreen(darkestPoint),
        contentImage.getColorModel().getBlue(darkestPoint)
      };
    System.out.println("referenceColor["+ referenceColor[0]+","+referenceColor[1]+","+referenceColor[2]+"]");
    convertToBinary(contentImage,referenceColor);
//    for (int x = contentImage.getMinX(); x < contentImage.getWidth(); x++) {
//      for (int y = contentImage.getMinY(); y < contentImage.getHeight(); y++) {
//        Object data = contentImage.getRaster().getDataElements(x, y, null);
//        int blue=contentImage.getColorModel().getBlue(data);
//        if(blue > gray[0])
//          contentImage.getRaster().setPixel(x,y,white);
////        if(blue <= gray[0] && y >= 384)
////          bufferedImage.getRaster().setPixel(x,y,white);
//        if(blue <= gray[0])
//          contentImage.getRaster().setPixel(x,y,black);
//      }
//    }
    BufferedImage birthImage=ImageHelper.getSubImage(bufferedImage,bufferedImage.getMinX(),154,414,54);
    darkestPoint = darkestPoint(birthImage);
    referenceColor=new int[] {
        birthImage.getColorModel().getRed(darkestPoint),
        birthImage.getColorModel().getGreen(darkestPoint),
        birthImage.getColorModel().getBlue(darkestPoint)
    };
    System.out.println("referenceColor["+ referenceColor[0]+","+referenceColor[1]+","+referenceColor[2]+"]");
    convertToBinary(birthImage,referenceColor);
//    for (int x = birthImage.getMinX(); x < birthImage.getWidth(); x++) {
//      for (int y = birthImage.getMinY(); y < birthImage.getHeight(); y++) {
//        Object data = birthImage.getRaster().getDataElements(x, y, null);
//        int blue=birthImage.getColorModel().getBlue(data);
//        if(blue > gray[0])
//          birthImage.getRaster().setPixel(x,y,white);
////        if(blue <= gray[0] && y >= 384)
////          bufferedImage.getRaster().setPixel(x,y,white);
//        if(blue <= gray[0])
//          birthImage.getRaster().setPixel(x,y,black);
//      }
//    }
    BufferedImage addressImage = ImageHelper.getSubImage(bufferedImage,bufferedImage.getMinX(),208,414,144);
    darkestPoint=darkestPoint(addressImage);
    referenceColor=new int[] {
        addressImage.getColorModel().getRed(darkestPoint),
        addressImage.getColorModel().getGreen(darkestPoint),
        addressImage.getColorModel().getBlue(darkestPoint)
    };
    System.out.println("referenceColor["+ referenceColor[0]+","+referenceColor[1]+","+referenceColor[2]+"]");

    convertToBinary(addressImage,referenceColor);
//    for (int x = addressImage.getMinX(); x < addressImage.getWidth(); x++) {
//      for (int y = addressImage.getMinY(); y < addressImage.getHeight(); y++) {
//        Object data = addressImage.getRaster().getDataElements(x, y, null);
//        int blue = addressImage.getColorModel().getBlue(data);
//        if (blue > gray[0])
//          addressImage.getRaster().setPixel(x, y, white);
////        if(blue <= gray[0] && y >= 384)
////          bufferedImage.getRaster().setPixel(x,y,white);
//        if (blue <= gray[0])
//          addressImage.getRaster().setPixel(x, y, black);
//      }
//    }

    BufferedImage idImage = ImageHelper.getSubImage(bufferedImage, bufferedImage.getMinX(), 354, bufferedImage.getWidth(), bufferedImage.getHeight() - 354);
    darkestPoint=darkestPoint(idImage);
    referenceColor=new int[] {
        idImage.getColorModel().getRed(darkestPoint),
        idImage.getColorModel().getGreen(darkestPoint),
        idImage.getColorModel().getBlue(darkestPoint)
    };
    System.out.println("referenceColor["+ referenceColor[0]+","+referenceColor[1]+","+referenceColor[2]+"]");

    convertToBinary(idImage,referenceColor);
//    for (int x = idImage.getMinX(); x < idImage.getWidth(); x++) {
//      for (int y = idImage.getMinY(); y < idImage.getHeight(); y++) {
//        Object data = idImage.getRaster().getDataElements(x, y, null);
//        int blue=idImage.getColorModel().getBlue(data);
//        if(blue > 40)
//          idImage.getRaster().setPixel(x,y,white);
//      }
//    }
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
//    for (int f = 73; f >= 57; f--) {
//    }
//    bufferedImage=ImageHelper.convertImageToBinary(bufferedImage);
//    ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(new FileOutputStream("/home/hooxin/1.jpg"));
  }


}
