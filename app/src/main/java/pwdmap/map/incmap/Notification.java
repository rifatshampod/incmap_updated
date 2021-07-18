package pwdmap.map.incmap;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Notification extends AppCompatActivity {

    private static final String TAG = "ReadAndWriteSnippets";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private Dialog loadingDialog;

    private RecyclerView recyclerView;
    private List<CategoryModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notifications");

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        recyclerView = findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

         list = new ArrayList<>();

        final CategoryAdapter adapter = new CategoryAdapter(list);
        recyclerView.setAdapter(adapter);

        loadingDialog.show();
        myRef.child("notification").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    list.add(dataSnapshot1.getValue(CategoryModel.class));
                }
                adapter.notifyDataSetChanged();
                loadingDialog.dismiss();
                //Toast.makeText(Category.this,dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Notification.this, "Database error!", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                loadingDialog.dismiss();
                finish();
            }
        });



    }
}
