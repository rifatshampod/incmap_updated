package pwdmap.map.incmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MiddleLayer extends AppCompatActivity {

    int menuItemNo = 0;
    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle_layer);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            int i =(int) b.get("name");
            menuItemNo = i;

            Fragment homeTemp = null;

            switch(menuItemNo){
                case 1: homeTemp = new BlogFragment();
                    break;
                case 2: homeTemp = new AboutFragment();
                    break;
                case 3: homeTemp = new MapsFragment();
                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,homeTemp).commit();
        }

        bnv=findViewById(R.id.bottomNavigation);

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment temp = null;

                switch(item.getItemId()){
                    case R.id.menu_home: temp = new HomeFragment();
                        break;

                    case R.id.menu_account: temp = new AccountFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,temp).commit();

                return true;
            }
        });
    }


}