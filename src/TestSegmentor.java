import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by zhangsq on 17-11-24.
 */
public class TestSegmentor {
    public static void main(String[] args) throws FileNotFoundException {
        String inputString = "我们很爱吃苹果，香蕉";
        Segmentor segmentor = new Segmentor("lexer2/resources/segfeat_PKU", "lexer2/dic_PKU");
        String[] result = segmentor.segString(inputString);
        for (String i : result) {
            System.out.println(i);
        }
    }
}
