import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code128.Code128LogicImpl;

public class Code128FontEncoder {

    /**
     * データ文字列からCode128フォント用のエンコード済み文字列を生成します。
     *
     * @param data エンコードしたいデータ文字列
     * @return フォントで描画するためのエンコード済み文字列
     */
    public String getEncodedFontString(String data) {
        // ステップ1: データを数値配列にエンコード
        int[] encodedValues = encodeToValues(data);

        // ステップ2: 数値配列をフォント用文字列にマッピング
        return mapValuesToFontString(encodedValues);
    }

    /**
     * Barcode4Jの内部ロジックを使い、Code128のエンコード結果を数値配列で取得します。
     */
    private int[] encodeToValues(String data) {
        Code128Bean bean = new Code128Bean();
        
        // ★★★ 修正点 ★★★
        // Code128LogicImplはデフォルトでコードセットを自動最適化するため、
        // `setCodeset` のような設定は不要です。
        
        // エンコードロジックの実装を取得
        Code128LogicImpl logic = new Code128LogicImpl(bean);
        
        // エンコードを実行し、結果を数値配列として返します
        return logic.encode(data);
    }

    /**
     * Code128の数値配列を、一般的なCode128フォント用の文字列に変換します。
     */
    private String mapValuesToFontString(int[] values) {
        StringBuilder fontString = new StringBuilder();
        for (int value : values) {
            // Code128のキャラクタセット値からフォント用の文字へのマッピングルール
            if (value >= 95) {
                fontString.append((char) (value + 105));
            } else {
                fontString.append((char) (value + 32));
            }
        }
        return fontString.toString();
    }

    public static void main(String[] args) {
        Code128FontEncoder encoder = new Code128FontEncoder();

        // --- 例: GS1-128 ---
        String gs1Data = "(01)94912345678902(10)ABC-123";
        String encodedGs1 = encoder.getEncodedFontString(gs1Data);
        
        System.out.println("データ: " + gs1Data);
        System.out.println("エンコード結果: " + encodedGs1);
    }
}