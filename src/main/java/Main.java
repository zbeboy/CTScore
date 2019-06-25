import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File imageFile = new File("src/main/resources/img/1.jpg");
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
//         ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setDatapath("src/main/resources/tessdata"); // path to tessdata directory
        instance.setLanguage("eng");
        List<String> configs = new ArrayList<>();
        configs.add("digits");
        instance.setConfigs(configs);
        instance.setTessVariable("tessedit_char_whitelist", "0123456789");
        try {
            Rectangle rect = new Rectangle(0, 0, 45, 18);
            String result = instance.doOCR(imageFile, rect);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }
}
