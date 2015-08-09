package gs.kar.justeatrecruitmenttest.model;

/**
 * Restaurant model represents a restaurant in listing.
 */
public class Restaurant {

	private final String id;
	private final String name;
	private final Logo logo;
	private final float rating;
	private final Cuisine[] typesOfFood;

	public Restaurant(String id, String name, Logo logo, float rating, Cuisine[] typesOfFood) {
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.rating = rating;
		this.typesOfFood = typesOfFood;
	}

	public String getName() {
		return name;
	}

	public Logo getLogo() {
		return logo;
	}

	public float getRating() {
		return rating;
	}

	public Cuisine[] getTypesOfFood() {
		return typesOfFood;
	}

	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final Restaurant that = (Restaurant) o;

		return id.equals(that.id);

	}

	@Override public int hashCode() {
		return id.hashCode();
	}
}
