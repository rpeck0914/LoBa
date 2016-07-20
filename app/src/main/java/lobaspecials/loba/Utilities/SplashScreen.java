package lobaspecials.loba.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import lobaspecials.loba.Activities.LobaMainActivity;
import lobaspecials.loba.R;

/**
 * Created by Robert Peck on 11/30/2015.
 */
public class SplashScreen extends Activity {
    //Splash screen class is for displaying the logo when the apps starts up.

    //Variable for the time the splash screen is displayed.
    private static final int SPLASH_INTERVAL = 2000;

    //On create override method sets the time the screen will display and then creates and starts the intent for the main activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, LobaMainActivity.class);
                startActivity(i);

                this.finish();
            }
            private void finish(){

            }
        }, SPLASH_INTERVAL);
    }
}
