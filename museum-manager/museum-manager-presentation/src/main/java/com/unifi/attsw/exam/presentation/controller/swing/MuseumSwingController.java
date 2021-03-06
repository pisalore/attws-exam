package com.unifi.attsw.exam.presentation.controller.swing;

import com.unifi.attsw.exam.core.controller.MuseumController;
import com.unifi.attsw.exam.core.service.MuseumManagerService;
import com.unifi.attsw.exam.core.service.exception.MuseumManagerServiceException;
import com.unifi.attsw.exam.presentation.view.swing.ExhibitionSwingView;
import com.unifi.attsw.exam.presentation.view.swing.MuseumSwingView;
import com.unifi.attsw.exam.repository.model.Exhibition;
import com.unifi.attsw.exam.repository.model.Museum;
import com.unifi.attsw.exam.repository.repository.exception.RepositoryException;

/**
 * 
 * Swing implementation for {@link MuseumController}.
 *
 */
public class MuseumSwingController implements MuseumController {
	/**
	 * The {@link MuseumManagerService} .
	 */
	private MuseumManagerService museumService;

	/**
	 * The {@link MuseumSwingView} for this controller.
	 */
	private MuseumSwingView museumView;

	/**
	 * The {@link ExhibitionSwingView} for this controller.
	 */
	private ExhibitionSwingView exhibitionView;

	public MuseumSwingController(MuseumManagerService museumService, MuseumSwingView museumView,
			ExhibitionSwingView exhibitionView) {
		this.museumService = museumService;
		this.museumView = museumView;
		this.exhibitionView = exhibitionView;
	}

	public void getAllMuseums() {
		try {
			museumView.showAllMuseums(museumService.getAllMuseums());
		} catch (RepositoryException ex) {
			museumView.showError("Impossibile to get museums.", null);
		}
	}

	public void getAllExhibitions() {
		try {
			exhibitionView.showAllExhibitions(museumService.getAllExhibitions());
		} catch (RepositoryException ex) {
			museumView.showError("Impossibile to get all exhibitions.", null);
		}
	}

	public void getAllMuseumExhibitions(String museumName) {
		try {
			Museum museum = museumService.getMuseumByName(museumName);
			exhibitionView.showMuseumExhibitions(museumService.getAllMuseumExhibitions(museum));
		} catch (MuseumManagerServiceException ex) {
			exhibitionView.showError("Impossibile to get all exhibitions.", null);
		}
	}

	public void saveMuseum(Museum museum) {
		try {
			museumService.saveMuseum(museum);
			museumView.museumAdded(museum);
		} catch (MuseumManagerServiceException ex) {
			museumView.showError("Impossibile to add Museum: ", museum);
		}
	}

	public void saveExhibition(String museumName, Exhibition exhibition) {
		try {
			museumService.addNewExhibition(museumName, exhibition);
			exhibitionView.exhibitionAdded(exhibition);
		} catch (MuseumManagerServiceException ex) {
			exhibitionView.showError("Impossible to add Exhibition: ", exhibition);
		}
	}

	public void deleteMuseum(Museum museum) {
		try {
			museumService.deleteMuseum(museum);
			museumView.museumRemoved(museum);
			
		} catch (MuseumManagerServiceException ex) {
			museumView.showError("Impossible to delete Museum: ", museum);
		}
	}

	public void deleteExhibition(Exhibition exhibition) {
		try {
			museumService.deleteExhibition(exhibition);
			exhibitionView.exhibitionRemoved(exhibition);
		} catch (MuseumManagerServiceException ex) {
			exhibitionView.showError("Impossible to delete Exhibition: ", exhibition);
		}
	}

	public void bookExhibitionSeat(Exhibition exhibition) {
		try {
			museumService.bookExhibitionSeat(exhibition);
			exhibitionView.exhibitionBooked();
		} catch (MuseumManagerServiceException ex) {
			exhibitionView.showError("Impossible to book a seat for Exhibition: ", exhibition);
		}
	}

	public void openExhibitionsDashboard() {
		exhibitionView.setVisible(true);
		museumView.setVisible(false);
	}

	public void openMuseumDashboard() {
		museumView.setVisible(true);
		exhibitionView.setVisible(false);
	}

}
