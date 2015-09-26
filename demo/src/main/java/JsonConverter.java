import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by joseph on 9/14/15.
 */
public class JsonConverter {
    public static void main(String[] args) {
        JsonConverter obj = new JsonConverter();
        String json = obj.readFileFromResources("/data.json");
        obj.convertJson(json);
    }

    private String readFileFromResources(String fileName) {
        String result = "";
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        try {
            result = IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void convertJson(String json) {
        Map<String, Object> map = new Gson().fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
        System.out.println(map);
        Object json2 = map.get("contacts");
        System.out.println(json2);

//        map = new Gson().fromJson((String)json2, new TypeToken<Map<String, Object>>() {}.getType());

//        System.out.println(map);




    }
}
