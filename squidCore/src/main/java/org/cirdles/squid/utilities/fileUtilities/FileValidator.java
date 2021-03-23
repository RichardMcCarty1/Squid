package org.cirdles.squid.utilities.fileUtilities;


import javax.xml.validation.Schema;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import static org.cirdles.squid.utilities.fileUtilities.TextFileUtilities.writeTextFileFromListOfStringsWithUnixLineEnd;
import org.xml.sax.SAXException;

public class FileValidator {

    /**
     *
     * @param serializedFile
     * @param schema
     * @param squid3ConstantHeader
     * @return
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public static void validateXML(File serializedFile, Schema schema, String squid3ConstantHeader) throws SAXException,
            IOException, ArrayIndexOutOfBoundsException {
        String[] headerArray = squid3ConstantHeader.split("\\n");
        File tempSerializedFile; // Temp file with corresponding header for XML validation
        List<String> lines = Files.readAllLines(serializedFile.toPath(), Charset.defaultCharset());
        if (!(lines.get(2).equals(headerArray[2]))) { // Does file already have header?
            // Change header
            lines.set(2, headerArray[2]);
            for (int x = 3; x < headerArray.length; x++) {
                lines.add(x, headerArray[x]);
            }
        }     
        tempSerializedFile = writeTextFileFromListOfStringsWithUnixLineEnd(lines, "squidTempXML", ".xml");
        Validator validator = schema.newValidator();
        Source source = new StreamSource(tempSerializedFile);
        validator.validate(source);
    }
    
    /**
     *
     * @param serializedFile
     * @param schema
     * @return
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public static void validateXML(File serializedFile, Schema schema) throws SAXException, IOException {
        File tempSerializedFile; // Temp file with corresponding header for XML validation
        
        // Exception thrown if file is xml but validation fails
        List<String> lines = Files.readAllLines(serializedFile.toPath(), Charset.defaultCharset());
        tempSerializedFile = writeTextFileFromListOfStringsWithUnixLineEnd(lines, "squidTempXML", ".xml");
        Validator validator = schema.newValidator();
        Source source = new StreamSource(tempSerializedFile);
        validator.validate(source);
    }
}
