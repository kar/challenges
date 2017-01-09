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
		userStory = Di.getDefault().inject(this);
		userStory.perform();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_default) {
			userStory = Di.getDefault().inject(this);
		} else if (id == R.id.action_dialog) {
			userStory = Di.getForDialogLocation().inject(this);
		} else if (id == R.id.action_map) {
			userStory = Di.getForMapLocation().inject(this);
		}

		userStory.perform();
		return true;
	}
}
