package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aarav
 */
public class YamlReader {

    String yamlString;

    public YamlReader(String filePath) {
        try {
            yamlString = IOUtils.toString(new FileInputStream(new File(filePath)));
        }
        catch (Exception e){
            System.out.println("File not found");
        }
    }

    public YamlReader(InputStream is) {
        try{
            yamlString = IOUtils.toString(is);
        }
        catch (Exception e){
            System.out.println("File not found");
        }

    }

    @Override
    public String toString(){
        return yamlString;
    }

    public String get(String nodePath){
        try {
            Object value = mapOfYamlString();
            for (String node : nodePath.split("\\.")) {
                value = ((Map) value).get(node);
            }
            return value.toString();
        }
        catch (Exception e){
            return null;
        }
    }

    public List<String> getList(String nodePath){
        Object value = mapOfYamlString();
        for(String node:nodePath.split("\\.")){
            value = ((Map)value).get(node);
        }
        return (ArrayList)value;
    }

    public Map<String, Object> getMap(String nodePath){
        Object value = mapOfYamlString();
        for(String node:nodePath.split("\\.")){
            value = ((Map)value).get(node);
        }
        System.out.println((HashMap)value);
        System.out.println(new JSONObject((HashMap)value).toString());
        return (HashMap)value;
    }

    String convertYamlToJson() throws IOException {
         String testdata=System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator+"testData" +File.separator + "testdata.yaml";

        String yaml=new String(Files.readAllBytes(Paths.get(
                testdata)));
        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        Object obj = yamlReader.readValue(yaml, Object.class);

        ObjectMapper jsonWriter = new ObjectMapper();
        System.out.println(jsonWriter.writeValueAsString(obj));
        return jsonWriter.writeValueAsString(obj);
    }

    private Map<String, Object> mapOfYamlString(){
        return (Map<String,Object>)(new Yaml()).load(yamlString);
    }
}
