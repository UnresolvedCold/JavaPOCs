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

public class MarkdownToPropertiesConverter {
  public static void main(String[] args) {
    String filePath = "application.properties.md";
    String markdown = readFileFromResources(filePath);
    MutableDataSet options = new MutableDataSet();
    Parser parser = Parser.builder(options).build();
    Node document = parser.parse(markdown);
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
        headingStack.push(key);

        String fullKey = String.join(".", headingStack);

        if (!value.isEmpty()) {
          properties.put(fullKey, value);
        }
      }
      current = current.getNext();
    }

    return properties;
  }
}