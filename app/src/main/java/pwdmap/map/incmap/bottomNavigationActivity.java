package pwdmap.map.incmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class bottomNavigationActivity extends AppCompatActivity {

    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,new HomeFragment()).commit();

        bnv=findViewById(R.id.bottomNavigation);

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment temp = null;

                switch(item.getItemId()){
                    case R.id.menu_home: temp = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,temp).commit();
                        break;

                    case R.id.menu_account: temp = new AccountFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,temp).commit();
                        break;

                    case R.id.menu_notification: Intent notificationIntent = new Intent(getBaseContext(), Notification.class);
                        startActivity(notificationIntent);
                        break;

                    case R.id.menu_logout: FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        break;

                   // case R.id.menu_assessment:temp = new AssessmentFragment();
                       // break;

                    //case R.id.menu_career:temp = new JobFragment();
                        //break;
                }



                return true;
            }
        });


    }
}