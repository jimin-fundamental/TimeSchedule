package TimeScheduler.project.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import TimeScheduler.project.domain.Task;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenAiService {
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

    private final String apiKey;
    private final OkHttpClient client;

    public OpenAiService() {
        this.apiKey = API_KEY;
        // Create OkHttpClient with a timeout of 300 seconds
        this.client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public List<String> fetchUpdatedSchedule(List<Task> fixedTasks, List<Task> flexibleTasks) throws IOException {
        List<String> assignedTasks = new ArrayList<>();

        JSONArray messagesArray = new JSONArray();
        int numOfFlex = flexibleTasks.size();
        System.out.println(numOfFlex);

        // Add system message for meal and sleep time constraints
        JSONObject systemMessage1 = new JSONObject();
        systemMessage1.put("role", "system");
        systemMessage1.put("content", "You are a helpful assistant. You have to schedule the member's time.");
        messagesArray.put(systemMessage1);

        JSONObject systemMessage2 = new JSONObject();
        systemMessage2.put("role", "system");
        systemMessage2.put("content", "You should consider people's meal and sleep time when scheduling.");
        messagesArray.put(systemMessage2);

        // Add user message for meal times
        JSONObject mealTimeMessage = new JSONObject();
        mealTimeMessage.put("role", "user");
        mealTimeMessage.put("content", "Meal times are from 12:00pm - 1:00pm and 7:00pm - 8:00pm.");
        messagesArray.put(mealTimeMessage);

        // Add user message for fixed tasks
        for (Task task : fixedTasks) {
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            String time = task.getStartTime() + " - " + task.getEndTime();
            userMessage.put("content", "Schedule a fixed task: " + task.getName() + ", Time: " + time);
            messagesArray.put(userMessage);
        }

        // Add rest period
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", "Schedule a rest period: 30 minutes");
        messagesArray.put(userMessage);

        // Add user messages for flexible tasks
        for (Task task : flexibleTasks) {
            JSONObject userMessageFlex = new JSONObject();
            userMessageFlex.put("role", "user");
            userMessageFlex.put("content", "Schedule a flexible task: " + task.getName() +
                    ", Duration: " + task.getDuration() +
                    ", Priority: " + task.getPriority());
            messagesArray.put(userMessageFlex);
        }

        JSONObject userMessageFormat = new JSONObject();
        userMessageFormat.put("role", "user");
        userMessageFormat.put("content", "Provide the scheduled times for each flexible task in the format 'TaskName: StartTime - EndTime\n'. You can assign all the flexible tasks only in time 7:00 - 22:00 (endtime also should be ended before 22:00) and say only 'TaskName: StartTime - EndTime', nothing more and only for tasks which have fixed = false.");
        messagesArray.put(userMessageFormat);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", messagesArray);

        System.out.println("message array 출력합니다!");
        //messagesArray console에 print
        for(Object m: messagesArray){
            System.out.println(m);
//            System.out.println(m.toString());
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody.toString());

        Request request = new Request.Builder()
                .url(OPENAI_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject responseJson = new JSONObject(responseBody);

                //responsebody 출력
                System.out.println("responsebody 출력합니다.");
                System.out.println(responseBody);//전체 메시지
                System.out.println("responsejson 출력합니다.");
                System.out.println(responseJson);//전체 메세지를 한줄로, 여기서 "content": ~가 유의미
                System.out.println( );

                JSONArray choicesArray = responseJson.getJSONArray("choices");
                System.out.println("choicesArray 출력합니다");
                for(Object m: choicesArray)
                    System.out.println(m);
                System.out.println();

                for (int i = 0; i < choicesArray.length(); i++) {
                    JSONObject choiceObject = choicesArray.getJSONObject(i);
                    if (choiceObject.has("message") && !choiceObject.isNull("message")) {
                        JSONObject messageObj = choiceObject.getJSONObject("message");
                        String completion = messageObj.getString("content");

                        System.out.println("numofFlex:"+numOfFlex);
                        assignedTasks = getAssignedTask(completion, numOfFlex);

                        System.out.println("AsssignedTask print합니다!");
                        for (String s: assignedTasks){
                            System.out.println(s);
                        }
                        System.out.println();

                        if (assignedTasks != null) {
                            System.out.println("이제 CalenderService로 return할게요~!");
                            System.out.println("_--------------------------------------------------_");
                            return assignedTasks;
                        }
                    } else {
                        System.out.println("Missing message field in the response.");
                    }
                }

            } else {
                System.out.println("Request failed. Response code: " + response.code());
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            System.out.println("Socket timeout exception");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO exception");
        }

        return assignedTasks;
    }

    private List<String> getAssignedTask(String completion, int numofFlex) {
        List<String> assignedTasks = new ArrayList<>();
        Pattern pattern = Pattern.compile("(.*?): (\\d+:\\d+\\s*) - (\\d+:\\d+\\s*)");
        Matcher matcher = pattern.matcher(completion);

        while (numofFlex > 0 && matcher.find()) {
            String taskName = matcher.group(1).trim();
            String startTime = matcher.group(2).trim();
            String endTime = matcher.group(3).trim();

            String assignedTask = "TaskName: " + taskName + ", StartTime: " + startTime + ", EndTime: " + endTime;
            System.out.println("This assignedTask is: " + assignedTask);
            assignedTasks.add(assignedTask);
            numofFlex--;
        }

        return assignedTasks;
    }





}
