package gs.kar.justeatrecruitmenttest.model;

import java.net.URL;

/**
 * Logo represents restaurant logo.
 *
 * It abstracts the way of how the logo should be fetched.
 */
public class Logo {

	private URL url;

	public Logo(URL url) {
		this.url = url;
	}
}
