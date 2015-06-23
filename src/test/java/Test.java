import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Test {
  public static void main(String[] args) throws TesseractException, IOException {
    File imageFile=new File("/home/hooxin/965362_4.jpg");


    Tesseract tesseract=new Tesseract();
    tesseract.setDatapath("/usr/share/tessdata/");
    tesseract.setLanguage("chi_sim");
    BufferedImage bufferedImage = ImageHelper.cloneImage(ImageIO.read(imageFile));
    bufferedImage=ImageHelper.convertImageToGrayscale(bufferedImage);
    ImageIO.write(bufferedImage,"jpg",new FileOutputStream("/home/hooxin/1.jpg"));
    int[] white=new int[]{255,255,255};
    int[] black=new int[] {0,0,0};
    int width=bufferedImage.getWidth();
    int height=bufferedImage.getHeight();
    for (int f = 140; f <= 189; f++) {
      for (int i = bufferedImage.getMinX(); i < width; i++) {
        for (int j = bufferedImage.getMinY(); j < height; j++) {
          Object dataElements = bufferedImage.getRaster().getDataElements(i, j, null);
          int blue = bufferedImage.getColorModel().getBlue(dataElements);
          int red = bufferedImage.getColorModel().getRed(dataElements);
          int green = bufferedImage.getColorModel().getGreen(dataElements);
          if(blue>=f && red>= f && green >= f)
            bufferedImage.getRaster().setPixel(i,j,white);
//        else
//          bufferedImage.getRaster().setPixel(i,j,black);
        }
      }
      ImageIO.write(bufferedImage,"jpg",new FileOutputStream("/home/hooxin/"+f+".jpg"));
      String result=tesseract.doOCR(bufferedImage);
      System.out.println(result);
    }
//    bufferedImage=ImageHelper.convertImageToBinary(bufferedImage);
//    ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(new FileOutputStream("/home/hooxin/1.jpg"));
  }
}
