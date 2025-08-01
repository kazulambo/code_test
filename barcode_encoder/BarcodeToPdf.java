import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class BarcodeToPdf {

    public static void main(String[] args) throws Exception {
        String barcodeData = "2129034231";
        File barcodeImage = new File("barcode.png");

        // 1. バーコードPNG画像を生成
        generateBarcodeImage(barcodeData, barcodeImage);

        // 2. PDFに貼り付け
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDImageXObject pdImage = PDImageXObject.createFromFile("barcode.png", document);
            float imageWidth = 200;
            float imageHeight = 50;
            float x = 100;
            float y = 700;

            try (var contentStream = new org.apache.pdfbox.pdmodel.PDPageContentStream(document, page)) {
                contentStream.drawImage(pdImage, x, y, imageWidth, imageHeight);
            }

            document.save("output.pdf");
        }

        System.out.println("✅ PDF生成完了：output.pdf");
    }

    public static void generateBarcodeImage(String data, File outputFile) throws IOException {
        Code128Bean bean = new Code128Bean();
        bean.setModuleWidth(0.21); // バーの幅（調整可能）
        bean.doQuietZone(false);   // Quiet Zoneなし（PDF側でスペース取るなら不要）

        // 画像出力設定（DPI, フォーマット）
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                new FileOutputStream(outputFile),
                "image/png",
                300,
                BufferedImage.TYPE_BYTE_BINARY,
                false,
                0
        );

        // データを描画
        bean.generateBarcode(canvas, data);
        canvas.finish();
    }
}
