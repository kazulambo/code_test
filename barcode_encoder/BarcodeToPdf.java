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
        String templatePath = "template.pdf";
        String outputPath = "output.pdf";
        String imagePath = "barcode.png";

        try (PDDocument document = PDDocument.load(new File(templatePath))) {
            PDAcroForm form = document.getDocumentCatalog().getAcroForm();
            PDField field = form.getField("barcodeImage"); // フィールド名はPDFで確認

            if (field != null) {
                PDAnnotationWidget widget = field.getWidgets().get(0);
                PDRectangle rect = widget.getRectangle();
                PDPage page = widget.getPage();

                // バーコード画像読み込み
                BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
                PDImageXObject image = PDImageXObject.createFromFile(imagePath, document);

                // フィールドの位置に画像を貼り付け
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
                    contentStream.drawImage(image, rect.getLowerLeftX(), rect.getLowerLeftY(), rect.getWidth(), rect.getHeight());
                }

                // フィールドを非表示にすることで画像だけ表示される
                widget.setPrinted(true);
                widget.setHidden(true);
            }

            document.save(outputPath);
            System.out.println("✅ PDF生成完了：" + outputPath);
        }
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
