package TimeScheduler.project.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import TimeScheduler.project.controller.Task;

import java.io.IOException;
import java.util.List;

@Transactional
public class OpenAiService {
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

    private final String apiKey;
    private final OkHttpClient client;

    public OpenAiService() {
        this.apiKey = API_KEY;
        this.client = new OkHttpClient();
    }

    public void fetchUpdatedSchedule(List<Task> fixedTasks, List<Task> flexibleTasks) throws IOException {
        JSONArray messagesArray = new JSONArray();

        // Add system message for meal time constraint
        JSONObject mealTimeSystemMessage = new JSONObject();
        mealTimeSystemMessage.put("role", "system");
        mealTimeSystemMessage.put("content", "You are a helpful assistant.");
        messagesArray.put(mealTimeSystemMessage);

        // Add user message for meal time constraint
        JSONObject mealTimeUserMessage = new JSONObject();
        mealTimeUserMessage.put("role", "user");
        mealTimeUserMessage.put("content", "Schedule a flexible task during meal time");
        messagesArray.put(mealTimeUserMessage);

        // Add user messages for each flexible task
        for (Task task : flexibleTasks) {
            if (!task.isFixed()) {
                JSONObject userMessage = new JSONObject();
                userMessage.put("role", "user");
                userMessage.put("content", "Schedule a flexible task: " + task.getName());
                messagesArray.put(userMessage);
            }
        }

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
                    Long taskId = getTaskIdByCompletion(flexibleTasks, completion);
                    String time = extractTimeFromCompletion(completion);

                    if (taskId != null) {
                        for (Task task : flexibleTasks) {
                            if (task.getId().equals(taskId)) {
                                task.setTime(time);
                                break;
                            }
                        }
                    }
                }
            } else {
                System.out.println("Failed to fetch updated schedule. Error: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Long getTaskIdByCompletion(List<Task> flexibleTasks, String completion) {
        // Implement logic to extract the task ID from the completion text
        // You can use a regular expression, parsing, or any other method based on the expected format of the completion text
        // Return the task ID if found, or null if not found

        for (Task task : flexibleTasks) {
            if (task.getName().equals(completion)) {
                return task.getId();
            }
        }
        return null;
    }

    private String extractTimeFromCompletion(String completion) {
        // Implement logic to extract the time from the completion text
        // Adjust the implementation based on the actual response format
        // Assuming the completion text is in the format: "Scheduled time: 8:00am - 9:30am"

        // Split the completion text by the ":" delimiter
        String[] parts = completion.split(":");

        if (parts.length > 1) {
            // Extract the time part after the ":" delimiter
            String timePart = parts[1].trim();

            // Split the time part by the "-" delimiter
            String[] timeRange = timePart.split("-");

            if (timeRange.length > 1) {
                // Extract the start and end time
                String startTime = timeRange[0].trim();
                String endTime = timeRange[1].trim();

                // Return the extracted time as a formatted string
                return startTime + " - " + endTime;
            }
        }

        // Return an empty string if extraction fails
        return "";
    }
}
