package gs.kar.justeatrecruitmenttest.action;

import java.util.List;

import gs.kar.justeatrecruitmenttest.model.Location;
import gs.kar.justeatrecruitmenttest.model.Restaurant;

/**
 *
 */
public interface FetchRestaurantsAction extends AsyncAction<Location, List<Restaurant>> {
}
