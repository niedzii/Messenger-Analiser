import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

public class DataOperator {

  public void initAnalise(String pathToFile) {
    // Declare JSONObject
    JSONObject jsonObject = loadJsonObjectFromFile(pathToFile);

    // Title of conversation
    System.out.println(
        "Name of this conversation is : \"" + getNameOfConversation(jsonObject) + "\"");

    // Part responsible for creating map of participants <String name, Long messageCount>
    Map<String, Long> participants = createMapOfParticipants(jsonObject);

    // Displaying all participants
    displayingParticipants(participants);

    // Part responsible for messages
    JSONArray messages = getMessages(jsonObject);
    System.out.println("Number of messages in total : " + messages.size());

    // Operations on first message
    displayFirstMessageInfo(messages);

    // Counting messages per user
    countMessagesPerUser(messages, participants);

    // Displaying number of messages sent
    displayMessageSentStatistics(participants);
  }

  private static JSONObject loadJsonObjectFromFile(String pathToFile) {
    // Read json from file
    FileParser json = new FileParser(pathToFile);
    // Declare and return JSONObject
    return json.parseFileToJsonObject();
  }

  private String getNameOfConversation(JSONObject jsonObject) {
    return (String) jsonObject.get("title");
  }

  private JSONArray getParticipantsAsJsonArray(JSONObject jsonObject) {
    return (JSONArray) jsonObject.get("participants");
  }

  private void displayMessageSentStatistics(Map<String, Long> participants) {
    // Iterate map to display data
    for (Map.Entry<String, Long> entry : participants.entrySet()) {
      System.out.println("User : " + entry.getKey() + " sent : " + entry.getValue() + " messages");
    }
  }

  private Map<String, Long> createMapOfParticipants(JSONObject jsonObject) {
    JSONArray participantsAsJson = getParticipantsAsJsonArray(jsonObject);
    Map<String, Long> participants = new TreeMap<>(); // Name and Messages Count
    for (int index = 0; index < participantsAsJson.size(); index++) {
      JSONObject js = (JSONObject) participantsAsJson.get(index);
      String name = (String) js.get("name");
      // Adding participants to the map with 0 messages
      participants.put(name, 0L);
    }
    return participants;
  }

  private JSONArray getMessages(JSONObject jsonObject) {
    return (JSONArray) jsonObject.get("messages");
  }

  private long countDaysFromFirstMessage(long timeOfFirstMessage) {
    return (System.currentTimeMillis() - timeOfFirstMessage) / 86400000;
  }

  private Map<String, Long> countMessagesPerUser(
      JSONArray messages, Map<String, Long> participants) {
    for (Object message : messages) {
      JSONObject js = (JSONObject) message;
      String name = (String) js.get("sender_name");
      long count = participants.get(name);
      participants.replace(name, ++count);
    }
    return participants;
  }

  private void displayingParticipants(Map<String, Long> participants) {
    int index = 1;
    for (Map.Entry<String, Long> entry : participants.entrySet()) {
      System.out.println(index + " : " + entry.getKey());
      index++;
    }
  }

  private String convertMsToDate(long timeInMs) {
    DateFormat format = new SimpleDateFormat("HH:mm:ss dd MMMMMMMMM yyyy");
    return format.format(timeInMs);
  }

  private void displayFirstMessageInfo(JSONArray messages) {
    JSONObject firstMessage = (JSONObject) messages.get(messages.size() - 1);
    long timeOfFirstMessage = (long) firstMessage.get("timestamp_ms"); // first message in ms
    String nameOfFirstSender = (String) firstMessage.get("sender_name");
    String contentOfFirst = (String) firstMessage.get("content");
    System.out.println(
        "First message was sent by "
            + nameOfFirstSender
            + " at "
            + convertMsToDate(timeOfFirstMessage));
    System.out.println("It was " + countDaysFromFirstMessage(timeOfFirstMessage) + " days ago");
    System.out.println(
        "It means there were "
            + messages.size() / countDaysFromFirstMessage(timeOfFirstMessage)
            + " messages per day");
    if (contentOfFirst != null) {
      System.out.println("It was : \"" + contentOfFirst + "\"");
    } else {
      System.out.println("First message is not available");
    }
  }
}
