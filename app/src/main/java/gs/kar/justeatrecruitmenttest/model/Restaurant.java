package gs.kar.justeatrecruitmenttest.model;

/**
 * Restaurant model represents a restaurant in listing.
 */
public class Restaurant {

	/**
	 * Name would be possibly localized in actual solution.
	 */
	private final String name;
	private final Logo logo;
	private final int rating;
	private final Cuisine[] typesOfFood;

	public Restaurant(String name, Logo logo, int rating, Cuisine[] typesOfFood) {
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

	public int getRating() {
		return rating;
	}

	public Cuisine[] getTypesOfFood() {
		return typesOfFood;
	}
}
