import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class main {

    public static void main(String[] args) {

        MsgFile json = new MsgFile("C:\\Users\\Niedz\\Desktop\\olek.json");

        //Declare JSONObject
        JSONObject obj = json.readJsonFromFile();

        //Title of conversation
        String conversationName = (String) obj.get("title");

        System.out.println("Name of this conversation is : \"" + conversationName + "\"");

        //Part responsible for participants
        JSONArray participantsAsJson = (JSONArray) obj.get("participants");
        System.out.println("Participants of this conversation are :");

        Map<String, Long> participants = new HashMap<>(); //Name and Messages Count

        //Adding participants to the map with 0 messages
        for (int index = 0; index < participantsAsJson.size(); index++) {
            JSONObject js = (JSONObject) participantsAsJson.get(index);
            String name = (String)js.get("name");
            System.out.println(index + 1 + " : " + name);
            participants.put(name, 0L);
        }

        //Part responsible for messages
        JSONArray messages = (JSONArray) obj.get("messages");
        System.out.println("Number of messages in total : " + messages.size());

        //Operations on first message
        JSONObject firstMessage = (JSONObject) messages.get(messages.size() - 1);
        long timeOfFirstMessage = (long) firstMessage.get("timestamp_ms"); //first message in ms
        String nameOfFirstSender = (String) firstMessage.get("sender_name");
        String contentOfFirst = (String) firstMessage.get("content");
        DateFormat simple = new SimpleDateFormat("HH:mm:ss MMMMMMMMM yyyy");
        Date dateOfFirstMessage = new Date(timeOfFirstMessage);
        System.out.println("First message was sent by " + nameOfFirstSender + " at " + simple.format(dateOfFirstMessage));
        if (contentOfFirst != null) {
            System.out.println("It was : \"" + contentOfFirst + "\"");
        }

        //Counting messages per user
        for (int index = 0; index < messages.size(); index++) {
            JSONObject js = (JSONObject) messages.get(index);
            String name = (String)js.get("sender_name");
            long count = participants.get(name);
            participants.replace(name, ++count);
        }

        //Displaying number of messages sent
        Iterator<Map.Entry<String, Long>> iterator = participants.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            System.out.println("User : " + entry.getKey() + " sent : " + entry.getValue() + " messages");
        }
    }
}
