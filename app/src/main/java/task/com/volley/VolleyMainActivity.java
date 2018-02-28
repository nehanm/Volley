package task.com.volley;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class VolleyMainActivity extends AppCompatActivity  {

    FrameLayout frameLayout;

    Button btgallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.fllayout);

        btgallery=findViewById(R.id.btgallery);



        getSupportFragmentManager().beginTransaction().add(R.id.fllayout,
                new VolleyFragment()).commit();


    }



    public void onGallery(View v)
    {


        getSupportFragmentManager().beginTransaction().replace(R.id.fllayout,
                new GalleryFragment())
                .addToBackStack(null)
                .commit();

    }


}
