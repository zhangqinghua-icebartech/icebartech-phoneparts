package com.icebartech.core.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;


public class QRCodeUtil {


    private static final String CHARSET = "UTF-8";
    private static final String FORMAT_NAME = "JPEG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 200;
    // LOGO宽度
    private static final int WIDTH = 60;
    // LOGO高度
    private static final int HEIGHT = 60;


    /**
     * 生成包含字符串信息的二维码图片
     *
     * @param content     二维码携带信息
     * @param qrCodeSize  二维码图片大小
     * @param imageFormat 二维码的格式
     * @throws WriterException
     * @throws IOException
     */
    public static ByteArrayOutputStream createQrCode(String content, int qrCodeSize, String imageFormat) {
        try {
            //设置二维码纠错级别ＭＡＰ
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
            // 矫错级别
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            //创建比特矩阵(位矩阵)的QR码编码的字符串
            BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);
            // 使BufferedImage勾画QRCode  (matrixWidth 是行二维码像素点)
            int matrixWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(matrixWidth - 200, matrixWidth - 200, BufferedImage.TYPE_INT_RGB);
            image.createGraphics();
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, matrixWidth, matrixWidth);
            // 使用比特矩阵画并保存图像
            graphics.setColor(Color.BLACK);
            for (int i = 0; i < matrixWidth; i++) {
                for (int j = 0; j < matrixWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i - 100, j - 100, 1, 1);
                    }
                }
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, imageFormat, outputStream);
            return outputStream;
        } catch (IOException | WriterException e) {
            throw new ServiceException(CommonResultCodeEnum.INTERFACE_INNER_INVOKE_ERROR, "二维码生成失败." + e.getMessage());
        }
    }

    /**
     * 读二维码并输出携带的信息
     */
    public static String readQrCode(InputStream inputStream) throws IOException {
        //从输入流中获取字符串信息
        BufferedImage image = ImageIO.read(inputStream);
        //将图像转换为二进制位图源
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap).getText();
        } catch (ReaderException e) {
            throw new ServiceException(CommonResultCodeEnum.INTERFACE_INNER_INVOKE_ERROR, "二维码解析失败." + e.getMessage());
        }
    }

    /**
     * user: Rex
     * date: 2016年12月29日  上午12:31:29
     * @param content 二维码内容
     * @param logoImgPath Logo
     * @param needCompress 是否压缩Logo
     * @return 返回二维码图片
     * @throws WriterException
     * @throws IOException
     * BufferedImage
     * TODO 创建二维码图片
     */
    public static BufferedImage createImage(String content, String logoImgPath) throws WriterException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (logoImgPath == null || "".equals(logoImgPath)) {
            return image;
        }

        return image;
    }

    /**
     * 二维码
     * @param code 参数
     * @return
     * @throws IOException
     * @throws WriterException
     */
    public static String qrCode(String code) throws IOException, WriterException {
        BufferedImage image = QRCodeUtil.createImage(code, null);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        boolean b = ImageIO.write(image, "jpg", bos);
        byte[] bytes = bos.toByteArray();
        byte[] base64 = Base64.encodeBase64(bytes);
        return "data:image/jpeg;base64," + new String(base64);
    }


    /**
     * 测试代码
     *
     * @throws WriterException
     */
    public static void main(String[] args) throws IOException, WriterException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File("d:\\qrcode.jpg"));
        ByteArrayOutputStream outputStream = createQrCode("WE1231238239128sASDASDSADSDWEWWREWRERWSDFDFSDSDF123123123123213123", 900, "JPEG");
        outputStream.writeTo(fileOutputStream);
        System.out.println(readQrCode(new FileInputStream(new File("d:\\qrcode.jpg"))));
    }
}
