package gs.kar.justeatrecruitmenttest.model;

/**
 * Cuisine model represents type of food available in restaurant.
 */
public class Cuisine {

	/**
	 * Unique identifier of the cuisine.
	 */
	private final String id;

	/**
	 * In this sample solution, it's just a lowercase english name of the cuisine.
	 * In actual solution, this would be localized from resources.
	 */
	private final String name;

	public Cuisine(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final Cuisine cuisine = (Cuisine) o;

		return id.equals(cuisine.id);

	}

	@Override public int hashCode() {
		return id.hashCode();
	}
}
