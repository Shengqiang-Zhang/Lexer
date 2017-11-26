import java.io.FileNotFoundException;

/**
 * Created by zhangsq on 17-11-24.
 */
public class TestPOSTagger {
    public static void main(String[] args) throws FileNotFoundException {
        String[] input = {"今天", "是", "个", "好", "日子"};
        POSTagger posTagger = new POSTagger("lexer2/resources/posfeat_pku");
        Grass.WordWithTag[] result = posTagger.tagSentence(input);
        for (Grass.WordWithTag i : result) {
            System.out.println(i);
        }
    }
}
