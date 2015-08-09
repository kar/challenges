package gs.kar.justeatrecruitmenttest.android.di;

/**
 * Di is a fictional dependency injection framework.
 */
public class Di {
	public static Module getModule() {
		// return new MockedModule();
		// return new SimpleModule();
		return new ProductionModule();
	}
}
