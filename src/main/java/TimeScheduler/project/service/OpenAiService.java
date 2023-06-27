package TimeScheduler.project.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import TimeScheduler.project.controller.Task;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
public class OpenAiService {
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

    private final String apiKey;
    private final OkHttpClient client;

    public OpenAiService() {
        this.apiKey = API_KEY;
        // Create OkHttpClient with a timeout of 300 seconds
        this.client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(300))
                .build();
    }


    public void fetchUpdatedSchedule(List<Task> fixedTasks, List<Task> flexibleTasks) throws IOException {
            JSONArray messagesArray = new JSONArray();

            // Add system message for meal and sleep time constraints
            JSONObject systemMessage1 = new JSONObject();
            systemMessage1.put("role", "system");
            systemMessage1.put("content", "You are a helpful assistant.");
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
                userMessage.put("content", "Schedule a fixed task: " + task.getName() + ", Time: " + task.getTime());
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
            userMessageFormat.put("content", "Provide the scheduled times for each flexible task in the format 'TaskName: StartTime - EndTime'.");
            messagesArray.put(userMessageFormat);

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", messagesArray);

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

                    JSONArray choicesArray = responseJson.getJSONArray("choices");

                    for (int i = 0; i < choicesArray.length(); i++) {
                        JSONObject choiceObject = choicesArray.getJSONObject(i);
                        String completion = choiceObject.getString("message");
                        String assignedTask = getAssignedTask(completion, flexibleTasks);

                        if (assignedTask != null) {
                            System.out.println(assignedTask);
                        }
                    }
                } else {
                    System.out.println("Request failed. Response code: " + response.code());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    private String getAssignedTask(String completion, List<Task> flexibleTasks) {
        // Use a regular expression to find matches in the format "TaskName: StartTime - EndTime".
        Pattern pattern = Pattern.compile("(.*): (.*am|pm) - (.*am|pm)");
        Matcher matcher = pattern.matcher(completion);

        // If a match is found, return the match. Otherwise, return null.
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }


}
