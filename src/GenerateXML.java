import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by zhangsq on 17-11-21.
 */
public class GenerateXML {
    public static void main(String[] args) throws IOException {
        GenerateXML generateXML = new GenerateXML();
        generateXML.generateXML("testFile/postaginput", "testFile/nerinput");
    }


    /**
     * preprocessing of text, combine POSTagOutput and NEROutput.
     *
     * @param postagFile 文件中每行为一句，每段间用空行隔开
     * @param nerFile    文件中每个词和ner的tag单独在一行
     * @param outputFile postagFile和nerFile合并的文件
     * @throws IOException
     */
    private void preprocessing(String postagFile, String nerFile, String outputFile) throws IOException {

        FileWriter fw = new FileWriter(outputFile);

        HashMap<String, String> mapNER = new HashMap<>();
        BufferedReader brPostag = new BufferedReader(new FileReader(postagFile));
        BufferedReader brNER = new BufferedReader(new FileReader(nerFile));
        String linePostag = null, lineNER = null;
        while ((lineNER = brNER.readLine()) != null) {
            String[] words = lineNER.split(" ");
            String seg = words[0];
            String tag = "/" + words[1];
            mapNER.put(seg, tag);

        }
        while ((linePostag = brPostag.readLine()) != null) {
            if (linePostag.length() == 0) {
                fw.write("\n");
                continue;
            }
            String[] words = linePostag.split(" ");
            int n = words.length;
            for (int i = 0; i < n; i++) {
                String[] unsepWord = words[i].split("/");
                String seg = unsepWord[0];
                String tag = "/" + unsepWord[1];
                if (Objects.equals(tag, "/NR")) {
                    if (mapNER.containsKey(seg)) {
                        tag = mapNER.get(seg);
                    }
                }
                fw.write(seg + tag + " ");
            }
            fw.write("\n");
        }
        fw.close();
    }

    /**
     * @param postagfile 文件中每行为一句，每段间用空行隔开
     * @param nerFile    文件中每个词和ner的tag单独在一行
     * @throws IOException
     */
    public void generateXML(String postagfile, String nerFile) throws IOException {
        String tempFile = "tempOutput";
        preprocessing(postagfile, nerFile, tempFile);
        Document document = DocumentHelper.createDocument();
        Element doc = document.addElement("doc");
        File inputfile = new File(tempFile);
        if (!inputfile.exists()) {
            throw new FileNotFoundException(tempFile);
        }
        InputStreamReader reader = new InputStreamReader(new FileInputStream(inputfile));
        BufferedReader br = new BufferedReader(reader);
        String lineText = br.readLine();
        int paragraphCount = 0;
        do {
            paragraphCount++;
            Element paragraph = doc.addElement("paragraph").addAttribute("id", String.valueOf(paragraphCount));
            int sentencesCount = 0;
            do {
                sentencesCount++;
                Element sentence = paragraph.addElement("sentence").addAttribute("id", String.valueOf(sentencesCount));
                String[] eachword = lineText.split(" ");
                int wordsCount = eachword.length;
                Element word = sentence.addElement("wordlist").addAttribute("length", String.valueOf(wordsCount));
                int nrCount = 0;
                ArrayList<Integer> index = new ArrayList<>();

                //print wordlist
                for (int i = 1; i <= wordsCount; i++) {
                    String seg = eachword[i - 1].split("/")[0];
                    String tag = eachword[i - 1].split("/")[1];
                    Element tok = word;
                    tok.addElement("tok").addAttribute("id", String.valueOf(i)).addAttribute("head", seg).addAttribute("type", tag);
                    if (tag.equals("LOC") || tag.equals("PER") || tag.equals("ORG")) {
                        nrCount++;
                        index.add(i - 1);

                    }
                }

                //print nrlist
                Element nrlist = sentence.addElement("nrlist");
                for (int i = 1; i <= nrCount; i++) {
                    Element tok = nrlist;
                    System.out.println("index = " + index);
                    tok.addElement("tok").addAttribute("id", String.valueOf(i)).addAttribute("head", eachword[index.get(i - 1)].split("/")[0]).addAttribute("type", eachword[index.get(i - 1)].split("/")[1]).addAttribute("start", String.valueOf(index.get(i - 1))).addAttribute("end", String.valueOf(index.get(i - 1)));
                }

                //print timelist
                //@TODO
                Element time = sentence.addElement("time");

                lineText = br.readLine();

            } while ((lineText != null) && (lineText.length() > 0)); //每行为一个句子
            while ((lineText != null) && (lineText.length() == 0)) { // 跳过空行
                lineText = br.readLine();
            }
        } while (lineText != null); //遇到空行则分段

        writeXML(document, "testFile/LexerXML");

    }


    private void writeXML(Document document, String outputFile) throws IOException {
        // let's write to a file
        try {
            FileWriter fw = new FileWriter(outputFile);
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(fw, format);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            System.err.println("Can not write outputFile!");
        }
    }
}
