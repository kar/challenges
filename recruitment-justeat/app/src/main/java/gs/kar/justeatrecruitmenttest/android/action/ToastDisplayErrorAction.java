package gs.kar.justeatrecruitmenttest.android.action;

import android.content.Context;
import android.widget.Toast;

import gs.kar.justeatrecruitmenttest.action.DisplayErrorAction;

/**
 *
 */
public class ToastDisplayErrorAction implements DisplayErrorAction {
	private final Context context;

	public ToastDisplayErrorAction(Context context) {
		this.context = context;
	}

	@Override public void perform(String message, Result<Void> result) {
		Toast.makeText(context, "Failed: " + message, Toast.LENGTH_LONG).show();
	}
}
