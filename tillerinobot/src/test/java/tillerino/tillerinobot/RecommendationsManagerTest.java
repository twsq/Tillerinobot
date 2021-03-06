package tillerino.tillerinobot;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;
import org.tillerino.osuApiModel.Mods;
import org.tillerino.osuApiModel.OsuApiUser;

import tillerino.tillerinobot.RecommendationsManager.Sampler.Settings;
import tillerino.tillerinobot.lang.Default;

public class RecommendationsManagerTest {
	TestBackend backend = new TestBackend(false);

	RecommendationsManager manager = new RecommendationsManager(backend);

	OsuApiUser user = new OsuApiUser();

	public RecommendationsManagerTest() throws SQLException, IOException {
		backend.hintUser("user", true, 1, 1000);

		user.setUserId(1);
	}

	@Test
	public void testPredicateParser() throws Exception {
		Settings samplerSettings = manager.parseSamplerSettings(user, "gamma AR=9", new Default());

		assertEquals(1, samplerSettings.predicates.size());

		// Test "nc" alias for nightcore
		// Gives double-time recommendations
		samplerSettings = manager.parseSamplerSettings(user, "nc", new Default());

		assertTrue(Mods.DoubleTime.is(samplerSettings.requestedMods));
	}

	@Test(expected = UserException.class)
	public void testContradiction() throws Exception {
		manager.parseSamplerSettings(user, "ar=1 ar=2", new Default());
	}
}
