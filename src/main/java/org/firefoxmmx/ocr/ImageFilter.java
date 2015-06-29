package org.firefoxmmx.ocr;

import com.sun.rowset.FilteredRowSetImpl;
import net.sourceforge.tess4j.util.ImageHelper;
import net.sourceforge.tess4j.util.ImageIOHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImageFilter {
  private static int[] WHITE = new int[]{255, 255, 255};
  private static int[] BLACK = new int[]{0, 0, 0};
  private static int[] GRAY = new int[]{60, 60, 60};

  public static Object findDarkestPoint(BufferedImage image) {
    Object darkestPoint = image.getRaster().getDataElements(image.getMinX(), image.getMinY(), null);
    for (int x = image.getMinX(); x < image.getWidth(); x++) {
      for (int y = image.getMinY(); y < image.getHeight(); y++) {
        Object data = image.getRaster().getDataElements(x, y, null);
        int dataBlue = image.getColorModel().getBlue(data);
        int dataRed = image.getColorModel().getRed(data);
        int dataGreen = image.getColorModel().getGreen(data);

        if (dataBlue < image.getColorModel().getBlue(darkestPoint) &&
            dataRed < image.getColorModel().getRed(darkestPoint) &&
            dataGreen < image.getColorModel().getGreen(darkestPoint))
          darkestPoint = data;
      }
    }

    return darkestPoint;
  }

  /**
   * 根据参考颜色,将图像二值化为黑白两种颜色
   *
   * @param image
   * @param rgb
   */
  public static void convertToBinary(BufferedImage image, int[] rgb) {
    for (int x = image.getMinX(); x < image.getWidth(); x++) {
      for (int y = image.getMinY(); y < image.getHeight(); y++) {
        Object data = image.getRaster().getDataElements(x, y, null);
        int blue = image.getColorModel().getBlue(data);
        int red = image.getColorModel().getRed(data);
        int green = image.getColorModel().getGreen(data);

        if (red > rgb[0] && green > rgb[1] && blue > rgb[2])
          image.getRaster().setPixel(x, y, WHITE);
        else
          image.getRaster().setPixel(x, y, BLACK);
      }
    }
  }

  public static BufferedImage cloneImage(BufferedImage image) {
    return ImageHelper.cloneImage(image);
  }

  public static BufferedImage subImage(BufferedImage image, int offsetX, int offsetY, int width, int height) {
    return ImageHelper.getSubImage(image, offsetX, offsetY, width, height);
  }

  public static  BufferedImage convertImageToGrayScale(BufferedImage image){
    return ImageHelper.convertImageToGrayscale(image);
  }

  public static BufferedImage imageScale(BufferedImage image, int width,int height) {
    return ImageHelper.getScaledInstance(image,width,height);
  }

  public static BufferedImage imageContrast(BufferedImage image,float rate) {
    for (int x = image.getMinX(); x < image.getWidth(); x++) {
      for (int y = image.getMinY(); y < image.getHeight(); y++) {
        Object data = image.getRaster().getDataElements(x, y, null);
        int dataRed = image.getColorModel().getRed(data);
        int dataBlue = image.getColorModel().getBlue(data);
        int dataGreen = image.getColorModel().getGreen(data);

        image.setRGB((dataRed << 24)*rate | dataGreen << 16 | dataBlue);
      }
    }
  }
}
