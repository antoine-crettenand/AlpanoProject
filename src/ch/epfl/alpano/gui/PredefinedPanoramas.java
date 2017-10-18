package ch.epfl.alpano.gui;

/**
 * A list of predefined Panoramas with specific parameters
 *
 * @author Antoine Crettenand (SCIPER 261768), Julien Sahli (SCIPER 272452)
 * @see ch.epfl.alpano.Panorama
 */
public interface PredefinedPanoramas {

	PanoramaUserParameters NIESEN = new PanoramaUserParameters(76_500, 467_300, 600, 180, 110, 300, 2500,
			800, 0);

	PanoramaUserParameters ALPES_DU_JURA = new PanoramaUserParameters(68_087, 470_085, 1380, 162, 27, 300,
			2500,
			800,
			0);

	PanoramaUserParameters MONT_RACINE = new PanoramaUserParameters(68_200, 470_200, 1500, 135, 45, 300,
			2500, 800, 0);

	PanoramaUserParameters FINSTERAARHORN = new PanoramaUserParameters(81_260, 465_374, 4300, 205, 20, 300,
			2500,
			800, 0);

	PanoramaUserParameters TOUR_DE_SAUVABELIN = new PanoramaUserParameters(66_385, 465_353, 700, 135, 100, 300,
			2500, 800, 0);

	PanoramaUserParameters PLAGE_DU_PELICAN = new PanoramaUserParameters(65_728, 465_132, 380, 135, 60, 300,
			2500,
			800 ,0);

	PanoramaUserParameters JUNGFRAU = new PanoramaUserParameters(77_300, 466_700, 3500, 124, 90, 300,2500,800,0);
}
