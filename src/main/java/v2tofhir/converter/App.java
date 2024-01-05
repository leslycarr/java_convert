package v2tofhir.converter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.Map;
import io.github.linuxforhealth.hl7.HL7ToFHIRConverter;


public class App implements RequestHandler<Map<String, Object>, String> {
    public String handleRequest(Map<String, Object> input, Context context) {
        System.out.println("Received input: " + input);

        String body = (String) input.get("body");
        System.out.println("Received body: " + body);

        // Extracting HL7 message from the JSON string in 'body'
        String hl7message = extractHL7Message(body);
        System.out.println("Extracted HL7 message: " + hl7message);

        if (hl7message == null || hl7message.isEmpty()) {
            return "HL7 message is missing or empty";
        }

        HL7ToFHIRConverter ftv = new HL7ToFHIRConverter();
        String fhirOutput = ftv.convert(hl7message);
        System.out.println("Converted FHIR output: " + fhirOutput);
        
        return fhirOutput;
    }

    private String extractHL7Message(String json) {
        // Basic extraction logic (assuming a simple structure)
        String key = "\"hl7message\":\"";
        int startIndex = json.indexOf(key);
        if (startIndex == -1) {
            return null;
        }
        startIndex += key.length();
        int endIndex = json.indexOf("\"", startIndex);
        if (endIndex == -1) {
            return null;
        }
        return json.substring(startIndex, endIndex).replace("\\n", "\n").replace("\\\\", "\\");
    }
}