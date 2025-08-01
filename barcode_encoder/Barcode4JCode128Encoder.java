import org.krysalis.barcode4j.impl.code128.Code128LogicImpl;
import org.krysalis.barcode4j.output.AbstractBarcodeLogicHandler;
import org.krysalis.barcode4j.output.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Barcode4JCode128Encoder {

    public static String encodeForCode128Font(String data) {
        List<Integer> codeValues = new ArrayList<>();

        Code128LogicImpl logic = new Code128LogicImpl(false); // false = not GS1 mode

        logic.generateBarcode(new AbstractBarcodeLogicHandler() {
            @Override
            public void startBarcode(String msg, String formattedMsg) {}

            @Override
            public void addBar(String name, int width, int height, int row, String orientation) {
                // NOOP
            }

            @Override
            public void startBarGroup(String name, String msg) {
                if (name != null && name.startsWith("char")) {
                    String codeStr = name.replace("char", "");
                    codeValues.add(Integer.parseInt(codeStr));
                }
            }

            @Override
            public void endBarGroup() {}

            @Override
            public void endBarcode() {}
        }, data);

        // フォントで使えるCode128文字列に変換（例：値 + 32）
        StringBuilder result = new StringBuilder();
        for (int code : codeValues) {
            result.append((char)(code + 32)); // 32は多くのフォントのオフセット
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String raw = "2129034231";  // 例：AI21 + データ8桁
        String encoded = encodeForCode128Font(raw);
        System.out.println("エンコード済み文字列: " + encoded);
    }
}
