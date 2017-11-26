
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;

/**
 * Created by zhangsq on 17-11-21.
 */
public class GenerateXML {
    public static void main(String[] args) throws IOException {
        GenerateXML generateXML = new GenerateXML();
        String fileName = "/home/zhangsq/Documents/Codes/IdeaProjects/lexer/lexer1/src/GenerateXML/segmentor_result.txt";
        generateXML.generateXMLForSegmentor(fileName, "UTF-8");
    }

    //to segmentor result
    public void generateXMLForSegmentor(String inputFile, String encoding) throws IOException {
        Document document = DocumentHelper.createDocument();
        Element doc = document.addElement("doc");
        File inputfile = new File(inputFile);
        if (!inputfile.exists()) {
            throw new FileNotFoundException(inputFile);
        }
        InputStreamReader read = new InputStreamReader(new FileInputStream(inputfile));
        BufferedReader br = new BufferedReader(read);
        String lineText = br.readLine();


        while (lineText != null) {
            if (lineText.length() == 0) {
                do {
                    lineText = br.readLine();
                } while (lineText.length() == 0);
            }

            Element paragraph = doc.addElement("paragraph");
            Element sentence = paragraph.addElement("sentence");
            while (lineText != null && lineText.length() > 0) {
                String[] s1 = lineText.split("\t");
                Element content = sentence.addText(s1[0] + "\\" + s1[1] + "\t");
                lineText = br.readLine();
            }

        }

        writeXML(document, "segmentXML");

    }

    public void generateXMLForPOSTagger(String inputFile) throws FileNotFoundException {
        File inputfile = new File(inputFile);
        if (!inputfile.exists()) {
            throw new FileNotFoundException(inputFile);
        }

    }

    public void generateXMLForNER(String inputFile) throws FileNotFoundException {
        File inputfile = new File(inputFile);
        if (!inputfile.exists()) {
            throw new FileNotFoundException(inputFile);
        }


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
