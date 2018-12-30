package cybernetics.lockandearn;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReadActivity extends AppCompatActivity {

    TextView totalPoints;
    long TotalPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        totalPoints=findViewById(R.id.readTotalPoint);

        //TotalPoints=40;
        Intent intent = getIntent();
        if(intent.hasExtra("totalPoints")){
            Bundle bundle = getIntent().getExtras();
            Long l = bundle.getLong("totalPoints");
            totalPoints.setText(Long.toString(l));
        }
    }
}
