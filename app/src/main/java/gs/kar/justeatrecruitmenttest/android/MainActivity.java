package gs.kar.justeatrecruitmenttest.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import gs.kar.justeatrecruitmenttest.R;
import gs.kar.justeatrecruitmenttest.android.di.Di;
import gs.kar.justeatrecruitmenttest.userstory.ViewListOfRestaurants;

/**
 * MainActivity is Android Studio generated activity which configures and performs our main user story.
 */
public class MainActivity extends AppCompatActivity {

	private ViewListOfRestaurants userStory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/**
		 * The user story configuration would be injected by DI in actual solution.
		 */
		userStory = Di.getModule().inject(this);
	}

	@Override protected void onResume() {
		super.onResume();
		userStory.perform();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
