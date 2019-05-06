import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;


/**
 * FORMAT OF JSON:
 {
 "participants": [
 {
 "name": "NAME OF FIRST PARTICIPANT"
 },
 {
 "name": "NAME OF SECOND PARTICIPANT"
 }
 ],
 "messages": [
 {
 "sender_id_INTERNAL": "",
 "sender_name": "NAME OF AUTHOR",
 "timestamp_ms": 1543434636641, (in UNIX ms)
 "content": "CONTENT",
 "type": "Generic"
 },
 {
 "sender_id_INTERNAL": "",
 "sender_name": "NAME OF AUTHOR",
 "timestamp_ms": 1543434636641, (in UNIX ms)
 "content": "CONTENT",
 "type": "Generic"
 }
 ],
 "title": "TITLE OF CONVERSATION",
 "is_still_participant": true,
 "thread_type": "Regular",
 "thread_path": "PATH TO THREAD"
 }
 **/

public class MsgFile {

    private String pathToFile;

    public String getPathToFile() {
        return pathToFile;
    }

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    MsgFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    JSONObject readJsonFromFile() {
        JSONParser parser = new JSONParser();
        try {
            Object object = parser.parse(new FileReader(pathToFile));
            return (JSONObject) object;
        } catch (Exception fe) {
            fe.printStackTrace();
        }
        return null;
    }


}