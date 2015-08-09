package gs.kar.justeatrecruitmenttest.android.action;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.widget.EditText;

import gs.kar.justeatrecruitmenttest.action.FetchLocationAction;
import gs.kar.justeatrecruitmenttest.model.Location;

/**
 * DialogFetchLocationAction asks user for postcode using Android Dialog window.
 */
public class DialogFetchLocationAction implements FetchLocationAction {

	private final Context context;
	private AlertDialog dialog;
	private String lastPostcode;

	public DialogFetchLocationAction(Context context) {
		this.context = context;
	}

	@Override public void perform(@Nullable Void input, @Nullable Result<Location> result) {
		if (dialog == null) {
			dialog = buildDialog(context);
		}

		showDialog(dialog, result);
	}

	private AlertDialog buildDialog(Context context) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Enter your postcode");

		final EditText input = new EditText(context);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
		builder.setView(input);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				lastPostcode = input.getText().toString();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				lastPostcode = null;
				dialog.cancel();
			}
		});

		return builder.create();
	}

	private void showDialog(AlertDialog dialog, final Result<Location> result) {
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override public void onDismiss(DialogInterface dialog) {
				reactToUserAction(result);
			}
		});

		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override public void onCancel(DialogInterface dialog) {
				reactToUserAction(result);
			}
		});

		dialog.show();
	}

	private void reactToUserAction(Result<Location> result) {
		if (lastPostcode != null) {
			result.on(getLocation(lastPostcode));
		} else {
			result.fail("no postcode provided");
		}
	}

	private Location getLocation(String postcode) {
		return new Location(postcode);
	}
}
