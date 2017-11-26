
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SemanticParser {
    private int handle;

    public SemanticParser(String semanticFeatureFile, String syntaxFeatureFile) throws FileNotFoundException {
        if (!new File(semanticFeatureFile).exists()) {
            throw new FileNotFoundException(semanticFeatureFile);
        }
        if (!new File(syntaxFeatureFile).exists()) {
            throw new FileNotFoundException(syntaxFeatureFile);
        }
        this.handle = Grass.INSTANCE.create_semantic_parser_ctx(
                semanticFeatureFile, syntaxFeatureFile);
    }

    public String parse(Grass.WordWithTag[] sentence) {
        String req = Arrays.stream(sentence)
                .map(Object::toString)
                .collect(Collectors.joining(" "));
        return Grass.INSTANCE.semantic_parse_string_with_ctx(this.handle,
                req, Grass.UTF8);
    }
}
