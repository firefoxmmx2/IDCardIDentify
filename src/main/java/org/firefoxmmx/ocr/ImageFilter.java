package org.firefoxmmx.ocr;

import com.sun.rowset.FilteredRowSetImpl;
import net.sourceforge.tess4j.util.ImageHelper;
import net.sourceforge.tess4j.util.ImageIOHelper;

import javax.crypto.AEADBadTagException;
import javax.imageio.ImageIO;
import java.awt.*;
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

  /**
   * 复制图片
   * @param image
   * @return
   */
  public static BufferedImage cloneImage(BufferedImage image) {
    return ImageHelper.cloneImage(image);
  }

  /**
   * 截取图片
   * @param image 原图片
   * @param offsetX 位置x
   * @param offsetY 位置y
   * @param width 宽度
   * @param height 高度
   * @return 截取的图片
   */
  public static BufferedImage subImage(BufferedImage image, int offsetX, int offsetY, int width, int height) {
    return ImageHelper.getSubImage(image, offsetX, offsetY, width, height);
  }

  /**
   * 图片统一灰度值转化
   * @param image
   * @return
   */
  public static  BufferedImage convertImageToGrayScale(BufferedImage image){
    return ImageHelper.convertImageToGrayscale(image);
  }

  /**
   * 图片缩放
   * @param image 图片
   * @param width 新的宽度
   * @param height 新的高度
   * @return 缩放后的图片
   */
  public static BufferedImage imageScale(BufferedImage image, int width,int height) {
    return ImageHelper.getScaledInstance(image, width, height);
  }

  /**
   * 图片对比度设置
   * @param image 原始图片
   * @param rate 对比率
   * @return 调整后的图片(引用原始图片)
   */
  public static BufferedImage imageContrast(BufferedImage image,float rate) {
    for (int x = image.getMinX(); x < image.getWidth(); x++) {
      for (int y = image.getMinY(); y < image.getHeight(); y++) {
        Object data = image.getRaster().getDataElements(x, y, null);
        int dataRed = image.getColorModel().getRed(data);
        int dataBlue = image.getColorModel().getBlue(data);
        int dataGreen = image.getColorModel().getGreen(data);
        int dataAlpha = image.getColorModel().getAlpha(data);
        Color dataColor = new Color(dataRed*rate,dataGreen*rate,dataBlue*rate,dataAlpha);
        image.setRGB(x,y,dataColor.getRGB());
      }
    }

    return image;
  }

  /**
   * 图片亮度调整
   * @param image
   * @param brightness
   * @return
   */
  public static BufferedImage imageBrightness(BufferedImage image,int brightness) {
    for (int x = image.getMinX(); x < image.getWidth(); x++) {
      for (int y = image.getMinY(); y < image.getHeight(); y++) {
        Object data = image.getRaster().getDataElements(x, y, null);
        int dataRed = image.getColorModel().getRed(data);
        int dataBlue = image.getColorModel().getBlue(data);
        int dataGreen = image.getColorModel().getGreen(data);
        int dataAlpha = image.getColorModel().getAlpha(data);
        int newRed = dataRed + brightness > 255 ? 255:dataRed+brightness;
        int newBlue = dataBlue + brightness > 255 ? 255: dataBlue+brightness;
        int newGreen = dataGreen + brightness > 255 ? 255: dataGreen+brightness;
        Color dataColor = new Color(newRed,newGreen,newBlue,dataAlpha);
        image.setRGB(x,y,dataColor.getRGB());
      }
    }

    return image;
  }

  /**
   * 获取图片亮度
   * @param image
   * @return
   */
  public static long imageBrightness(BufferedImage image){
    long totalBrightness = 0;
    long totalRed = 0;
    long totalGreen = 0;
    long totalBlue=0;
    for (int x = image.getMinX(); x < image.getWidth(); x++) {
      for (int y = image.getMinY(); y < image.getHeight(); y++) {
        Object data = image.getRaster().getDataElements(x, y, null);
        int dataRed = image.getColorModel().getRed(data);
        int dataBlue = image.getColorModel().getBlue(data);
        int dataGreen = image.getColorModel().getGreen(data);
        int dataAlpha = image.getColorModel().getAlpha(data);
        totalRed += dataRed;
        totalGreen += dataGreen;
        totalBlue += dataBlue;
//        totalBrightness += dataColor.getRGB();
      }
    }

    float avgRed = totalRed / (image.getHeight()*image.getWidth());
    float avgGreen = totalGreen / (image.getWidth() * image.getHeight());
    float avgBlue  =totalBlue / (image.getWidth() * image.getHeight());

    long avgBrightNess = totalBrightness / ( image.getWidth() * image.getHeight());

    return avgBrightNess;
  }
}
