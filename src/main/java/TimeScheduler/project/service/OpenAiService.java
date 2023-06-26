package TimeScheduler.project.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import TimeScheduler.project.controller.Task;

import java.io.IOException;
import java.util.Comparator;
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

    public void fetchUpdatedSchedule(List<Task> flexibleTasks) throws IOException {
        JSONArray tasksArray = new JSONArray();
        for (Task task : flexibleTasks) {
            JSONObject taskObject = new JSONObject();
            taskObject.put("name", task.getName());
            taskObject.put("duration", task.getDuration());
            taskObject.put("priority", task.getPriority());
            tasksArray.put(taskObject);
        }

        flexibleTasks.sort(Comparator.comparingInt(Task::getPriority));

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
                    int priority = updatedTaskObject.getInt("priority");
                    String time = updatedTaskObject.getString("time");

                    for (Task task : flexibleTasks) {
                        if (task.getId().equals(taskId)) {
                            task.setDuration(duration);
                            task.setTime(time);
                            break;
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
}
