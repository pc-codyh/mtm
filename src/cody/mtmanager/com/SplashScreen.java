package cody.mtmanager.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		
		delaySplashScreen();
	}
	
	// Display the splash screen for five seconds and then
	// move on to the main menu.
	private void delaySplashScreen()
	{
		Thread thread_timer = new Thread()
		{
			public void run()
			{
				try
				{
					// Sleep for five seconds.
					sleep(5000);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					// Load up the main menu.
					startActivity(new Intent("cody.mtmanager.com.MainMenu"));
				}
			}
		};
		
		thread_timer.start();
	}
}
