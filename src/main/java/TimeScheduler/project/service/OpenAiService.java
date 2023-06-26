package TimeScheduler.project.service;

import TimeScheduler.project.controller.Task;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Transactional
public class OpenAiService {
    private static final String OPENAI_URL = "https://api.openai.com/v1/images/generations";
    private static final String API_KEY = "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";

    private final String apiKey;
    private final OkHttpClient client;

    public OpenAiService() {
        this.apiKey = API_KEY;
        this.client = new OkHttpClient();
    }

    public List<Task> fetchUpdatedSchedule(List<Task> flexibleTasks) throws IOException {
        JSONArray tasksArray = new JSONArray();
        for (Task task : flexibleTasks) {
            JSONObject taskObject = new JSONObject();
            taskObject.put("name", task.getName());
            taskObject.put("duration", task.getDuration());
            tasksArray.put(taskObject);
        }

        JSONObject requestBody = new JSONObject();
        requestBody.put("tasks", tasksArray);

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

                JSONArray updatedTasksArray = responseJson.getJSONArray("updatedTasks");

                for (int i = 0; i < updatedTasksArray.length(); i++) {
                    JSONObject updatedTaskObject = updatedTasksArray.getJSONObject(i);
                    String taskId = updatedTaskObject.getString("id");
                    int duration = updatedTaskObject.getInt("duration");
                    String time = updatedTaskObject.getString("time");

                    // Find the task in the original task list and update its details
                    for (Task task : flexibleTasks) {
                        if (task.getId().equals(taskId)) {
                            task.setDuration(duration);
                            task.setTime(time);
                            break;
                        }
                    }
                }
            } else {
                // Handle error response
                System.out.println("Failed to fetch updated schedule. Error: " + response.code());
            }
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
        }

        return flexibleTasks;
    }

}
