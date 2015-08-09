package gs.kar.justeatrecruitmenttest.action;

import android.support.annotation.Nullable;

/**
 * AsyncAction describes asynchronously executed action which may fail.
 */
public interface AsyncAction<T, U> {

	interface Result<U> {
		void on(U result);
		void fail(String reason);
	}

	void perform(@Nullable T input, @Nullable Result<U> result);
}
