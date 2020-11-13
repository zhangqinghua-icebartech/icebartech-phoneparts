package com.icebartech.core.utils;

import com.itextpdf.kernel.geom.AffineTransform;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import java.io.IOException;

/**
 * iText7 操作PDF
 * https://github.com/CuteXiaoKe/iText7-examples
 */
public class PDFUtil {

    private static int pageNum = 1;

    /**
     * PDF分裂
     * https://blog.csdn.net/u012397189/article/details/78742207
     *
     * @param sourcePdf
     * @param destPdf
     * @param row       分割成row行
     * @param column    分割成column列
     * @throws IOException
     */
    public static void tiling(PdfDocument sourcePdf, PdfDocument destPdf, int row, int column) throws IOException {
        assert row < 1 || column < 1;
        PdfPage origPage = sourcePdf.getPage(pageNum);
        PdfFormXObject pageCopy = origPage.copyAsFormXObject(destPdf);
        // 这里取得的文档大小单位为pt  1in(英寸) = 2.54cm = 25.4mm = 72pt(点) = 6pc(派卡)
        Rectangle orig = origPage.getPageSize();

        // 保持原文档宽度 对分割后每个块进行放大
        AffineTransform transformationMatrix = AffineTransform.getScaleInstance(column, column);
        PageSize pageSize = new PageSize(orig.getWidth(), orig.getHeight() * (column * 1f / row));

        // 不进行放大 以原文档大小分割后单个块大小作为新文档页面大小
        // AffineTransform transformationMatrix = AffineTransform.getScaleInstance(1, 1);
        // PageSize pageSize = new PageSize(orig.getWidth() / column, orig.getHeight() / row);

        PdfPage page;
        PdfCanvas canvas;
        float x, y;
        for (int i = row; i > 0; i--) {
            for (int j = 1; j <= column; j++) {
                // 计算X轴于Y轴偏移量 坐标系形式
                x = -orig.getWidth() / column * (j - 1);
                y = -orig.getHeight() / row * (i - 1);
                page = destPdf.addNewPage(pageSize);
                canvas = new PdfCanvas(page);
                canvas.concatMatrix(transformationMatrix);
                canvas.addXObject(pageCopy, x, y);
            }
        }
        int pages = sourcePdf.getNumberOfPages();
        if (pages >= ++pageNum) {
            tiling(sourcePdf, destPdf, row, column);
        }
        if (pages == pageNum - 1) {
            destPdf.close();
            sourcePdf.close();
        }
    }

    /**
     * PDF分裂
     *
     * @param sourcePdfPath
     * @param destPdfPath
     * @param row           分割成row行
     * @param column        分割成column列
     * @throws IOException
     */
    public static void tiling(String sourcePdfPath, String destPdfPath, int row, int column) throws IOException {
        PdfDocument destPdf = new PdfDocument(new PdfWriter(sourcePdfPath));
        PdfDocument sourcePdf = new PdfDocument(new PdfReader(destPdfPath));
        tiling(sourcePdf, destPdf, row, column);
    }

    public static void main(String[] args) throws IOException {
        String destPdfPath = "D:/reference_out.pdf";
        String sourcePdfPath = "D:/reference.pdf";
        tiling(destPdfPath, sourcePdfPath, 3, 2);
    }

}
