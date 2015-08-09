package gs.kar.justeatrecruitmenttest.model;

/**
 * Cuisine model represents type of food available in restaurant.
 */
public class Cuisine {

	/**
	 * Unique identifier of the cuisine.
	 *
	 * In this sample solution, it's just a lowercase english name of the cuisine.
	 * In actual solution, this would be localized from resources.
	 */
	private final String name;

	public Cuisine(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final Cuisine cuisine = (Cuisine) o;

		return name.equals(cuisine.name);

	}

	@Override public int hashCode() {
		return name.hashCode();
	}
}
