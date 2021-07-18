package pwdmap.map.incmap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Article extends AppCompatActivity {

    private TextView title, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);

        Intent intent = getIntent();
        String titleName = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        title.setText(titleName);
        desc.setText(description);

    }
}