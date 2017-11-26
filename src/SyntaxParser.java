
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class SyntaxParser {
    private int handle;

    public SyntaxParser(String feature_file) throws FileNotFoundException {
        if (!new File(feature_file).exists()) {
            throw new FileNotFoundException(feature_file);
        }
        this.handle = Grass.INSTANCE.create_syntax_parser_ctx(feature_file);
    }

    public String parse(Grass.WordWithTag[] sentence) {
        String req = Arrays.stream(sentence)
                .map(Object::toString)
                .collect(Collectors.joining(" "));
        return Grass.INSTANCE.syntax_parse_string_with_ctx(this.handle,
                req, Grass.UTF8);
    }
}
