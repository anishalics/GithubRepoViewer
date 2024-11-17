package com.anishali.githubrepoviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername;
    private Button btnFetch;
    private RecyclerView recyclerView;
    private RepoAdapter repoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUsername = findViewById(R.id.et_username);
        btnFetch = findViewById(R.id.btn_fetch);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GitHubService service = retrofit.create(GitHubService.class);

        btnFetch.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();

            if (username.isEmpty()) {
                Toast.makeText(MainActivity.this, "Enter a username!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call the GitHub API to fetch repos
            Call<List<Repo>> call = service.listRepos(username);
            call.enqueue(new Callback<List<Repo>>() {
                @Override
                public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                    if (response.code() == 403) {
                        // Handle rate limit exceeded error
                        Toast.makeText(MainActivity.this, "Rate limit exceeded. Try again later.", Toast.LENGTH_LONG).show();
                    } else if (response.isSuccessful() && response.body() != null) {
                        // If response is successful and not empty
                        List<Repo> repos = response.body();
                        repoAdapter = new RepoAdapter(repos);
                        recyclerView.setAdapter(repoAdapter);
                    } else {
                        // Handle other response errors
                        Toast.makeText(MainActivity.this, "User not found or error in fetching repos!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Repo>> call, Throwable t) {
                    // Handle network or other failures
                    Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}