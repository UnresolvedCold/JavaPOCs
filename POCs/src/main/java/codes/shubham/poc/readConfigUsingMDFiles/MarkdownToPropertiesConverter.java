package codes.shubham.poc.readConfigUsingMDFiles;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownToPropertiesConverter {
  public static void main(String[] args) {
    String filePath = "application.properties.md";  // Replace with your file name
    String markdown = readFileFromResources(filePath);

    // Configure the parser
    MutableDataSet options = new MutableDataSet();
    Parser parser = Parser.builder(options).build();

    // Parse the markdown
    Node document = parser.parse(markdown);

    // Extract application properties
    Map<String, String> properties = extractProperties(document);

    // Print properties to verify
    properties.forEach((key, value) -> System.out.println(key + ": " + value));
  }

  private static String readFileFromResources(String fileName) {
    StringBuilder content = new StringBuilder();
    try (InputStream is = MarkdownToPropertiesConverter.class.getClassLoader().getResourceAsStream(fileName);
         BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        content.append(line).append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return content.toString();
  }

  private static Map<String, String> extractProperties(Node document) {
    Map<String, String> properties = new HashMap<>();
    Node current = document.getFirstChild();

    Stack<String> headingStack = new Stack<>();
    Pattern pattern = Pattern.compile("\\$\\{([^:}]+):([^}]+)\\}");

    while (current != null) {
      if (current instanceof Heading) {
        Heading heading = (Heading) current;
        String headingText = heading.getText().toString().trim();
        String[] parts = headingText.split(":", 2);
        String key = parts[0].trim();
        String value = parts.length > 1 ? parts[1].trim() : "";

        // Maintain stack to create the full key path
        while (!headingStack.isEmpty() && headingStack.size() >= heading.getLevel()) {
          headingStack.pop();
        }
        headingStack.push(key.toLowerCase());

        String fullKey = String.join(".", headingStack);

        if (!value.isEmpty()) {
          Matcher matcher = pattern.matcher(value);
          if (matcher.find()) {
            String envVar = matcher.group(1);
            String defaultValue = matcher.group(2);
            String envValue = System.getenv(envVar);
            value = envValue != null ? envValue : defaultValue;
          }
          properties.put(fullKey, value);
        }
      }
      current = current.getNext();
    }

    return properties;
  }
}