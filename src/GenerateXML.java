import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by zhangsq on 17-11-21.
 */
public class GenerateXML {
    public static void main(String[] args) throws IOException {
        GenerateXML generateXML = new GenerateXML();
        generateXML.generateXML("testFile/postaginput", "testFile/nerinput");
    }


    //preprocessing of text, combine POSTagOutput and NEROutput.
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
                fw.write(seg + " " + tag + " ");
            }
            fw.write("\n");
        }
        fw.close();
    }

    public void generateXML(String postagfile, String nerFile) throws IOException {
        String tempFile = "temOutput";
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
        do {
            Element paragraph = doc.addElement("paragraph");
            do {
                Element sentence = paragraph.addElement("sentence");
                Element content = sentence.addText(lineText);
                lineText = br.readLine();

            } while ((lineText != null) && (lineText.length() > 0)); //每行为一个句子
            while ((lineText != null) && (lineText.length() == 0)) { // 跳过空行
                lineText = br.readLine();
            }
        } while (lineText != null); //遇到空行则分段

        writeXML(document, "testFile/segmentXML");

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
