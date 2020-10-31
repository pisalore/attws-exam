package com.unifi.attsw.exam.test.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.*;

import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.VerificationCollector;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static java.util.Arrays.asList;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.MuseumRepository;
import com.unifi.attsw.exam.service.MuseumManagerService;
import com.unifi.attsw.exam.service.impl.MuseumManagerServiceImpl;
import com.unifi.attsw.exam.transaction.manager.TransactionManager;
import com.unifi.attsw.exam.transaction.manager.code.MuseumTransactionCode;
import com.unifi.attsw.exam.transaction.manager.code.ExhibitionTransactionCode;
import com.unifi.attsw.exam.transaction.manager.code.TransactionCode;

@RunWith(MockitoJUnitRunner.class)
public class MuseumManagerTest {

	private static final String MUSEUM1_TEST = "museum1_test";
	private static final String MUSEUM2_TEST = "museum2_test";

	private static final UUID MUSEUM_ID_1 = UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7");

	private static final String EXHIBITION1_TEST = "exhibition1_test";
	private static final String EXHIBITION2_TEST = "exhibition2_test";

	private static final int NUM_CONSTANT1 = 10;

	@Mock
	private TransactionManager transactionManager;

	@Mock
	private MuseumRepository museumRepository;

	@Mock
	private ExhibitionRepository exhibitionRepository;

	@Spy
	private Museum museum = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);

	@Spy
	private Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);

	@Rule
	public VerificationCollector collector = MockitoJUnit.collector();

	private MuseumManagerService museumManager;

	private InOrder inOrder;

	@Before
	public void setUp() throws RepositoryException {
		when(transactionManager.doInTransactionMuseum(any()))
				.thenAnswer(answer((MuseumTransactionCode<?> code) -> code.apply(museumRepository)));

		when(transactionManager.doInTransactionExhibition(any()))
				.thenAnswer(answer((ExhibitionTransactionCode<?> code) -> code.apply(exhibitionRepository)));

		when(transactionManager.doInTransaction(any()))
				.thenAnswer(answer((TransactionCode<?> code) -> code.apply(museumRepository, exhibitionRepository)));

		museumManager = new MuseumManagerServiceImpl(transactionManager);

		museum.setId(MUSEUM_ID_1);

		inOrder = inOrder(exhibitionRepository, museumRepository, museum, exhibition);

	}

	@Test
	public void testGetAllMuseumsWhenNoArePersisted() throws RepositoryException {
		when(museumRepository.findAllMuseums()).thenReturn(asList());
		List<Museum> museums = museumManager.getAllMuseums();
		verify(museumRepository).findAllMuseums();
		assertThat(museums).isEmpty();
	}

	@Test
	public void testGetAllMuseumsWhenMuseumsArePersisted() throws RepositoryException {
		Museum museum1 = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);
		Museum museum2 = createTestMuseum(MUSEUM2_TEST, NUM_CONSTANT1);
		when(museumRepository.findAllMuseums()).thenReturn(asList(museum1, museum2));
		List<Museum> museums = museumManager.getAllMuseums();
		verify(museumRepository).findAllMuseums();
		assertThat(museums).containsAll(asList(museum1, museum2));
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testSaveNullMuseumShouldThrow() {
		assertThatThrownBy(() -> {
			museumManager.saveMuseum(null);
			doThrow(new NullPointerException()).doNothing();
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossibile to add Museum.");
	}

	@Test
	public void testSaveNewMuseum() throws RepositoryException {
		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(null);
		museumManager.saveMuseum(museum);
		inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		inOrder.verify(museumRepository).addMuseum(museum);
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testSaveMuseumWhenMuseumExistsUpdate() throws RepositoryException {
		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum);
		museumManager.saveMuseum(museum);
		verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		verify(museumRepository).updateMuseum(museum);
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testDeleteMuseumWithNoExhibitions() throws RepositoryException {
		when(exhibitionRepository.findExhibitionsByMuseumId(museum.getId())).thenReturn(asList());
		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum);
		museumManager.deleteMuseum(museum);

		inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		inOrder.verify(exhibitionRepository).findExhibitionsByMuseumId(museum.getId());
		verify(museumRepository).deleteMuseum(museum);
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testDeleteMuseumWithExhibitions() throws RepositoryException {
		Exhibition exhibition1 = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		Exhibition exhibition2 = createExhibition(EXHIBITION2_TEST, NUM_CONSTANT1);

		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum);
		when(exhibitionRepository.findExhibitionsByMuseumId(museum.getId()))
				.thenReturn(asList(exhibition1, exhibition2));
		museumManager.deleteMuseum(museum);

		inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		inOrder.verify(exhibitionRepository).findExhibitionsByMuseumId(museum.getId());
		inOrder.verify(exhibitionRepository).deleteExhibition(exhibition1);
		inOrder.verify(exhibitionRepository).deleteExhibition(exhibition2);
		inOrder.verify(museumRepository).deleteMuseum(museum);
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testDeleteNullMuseumShouldThrow() throws RepositoryException {
		assertThatThrownBy(() -> {
			museumManager.deleteMuseum(null);
			doThrow(new NullPointerException()).doNothing();
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to delete Museum.");

	}

	@Test
	public void testDeleteMuseumWhichDoesNotExistShouldThrow() throws RepositoryException {
		assertThatThrownBy(() -> {
			museumManager.deleteMuseum(museum);
			when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(null);
			doThrow(new NoSuchElementException("The selected museum does not exist!")).doNothing();

		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to delete Museum.");

	}

	@Test
	public void testAddNewExhibitionWhenRoomsAreNotAvailableShouldThrow() throws RepositoryException {
		museum.setOccupiedRooms(NUM_CONSTANT1);

		assertThatThrownBy(() -> {
			when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum);
			museumManager.addNewExhibition(museum.getName(), exhibition);
			doThrow(new IllegalArgumentException("Impossibile to add new Exhibition: all rooms are occupied!"))
					.doNothing();

			inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
			inOrder.verify(exhibition).setMuseumId(museum.getId());
			inOrder.verify(museum).getOccupiedRooms();
			inOrder.verify(museum).getTotalRooms();
			verifyNoMoreInteractions(museumRepository, exhibitionRepository);
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to add Exhibition.");
	}

	@Test
	public void testAddNewExhibitionWhenRoomsAreAvailable() throws RepositoryException {
		int ouccupiedRooms = museum.getOccupiedRooms();
		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum);
		museumManager.addNewExhibition(museum.getName(), exhibition);

		inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		inOrder.verify(exhibition).setMuseumId(museum.getId());
		inOrder.verify(museum).getOccupiedRooms();
		inOrder.verify(museum).getTotalRooms();
		inOrder.verify(museum).setOccupiedRooms(ouccupiedRooms + 1);
		inOrder.verify(exhibitionRepository).addNewExhibition(exhibition);
		verifyNoMoreInteractions(museumRepository, exhibitionRepository);
	}

	@Test
	public void testAddNewExhibitionToAMuseumWhichDoesNotExistShouldThrow() throws RepositoryException {
		Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);

		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(null);
		assertThatThrownBy(() -> {
			museumManager.addNewExhibition(MUSEUM1_TEST, exhibition);
			doThrow(new NoSuchElementException("The selected museum does not exist!")).doNothing();
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to add Exhibition.");
	}

	@Test
	public void testDeleteNullExhibitionShouldThrow() throws RepositoryException {
		assertThatThrownBy(() -> {
			museumManager.deleteExhibition(null);
			doThrow(new NullPointerException()).doNothing();
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to delete Exhibition.");

	}

	@Test
	public void testDeleteExhibitionhichDoesNotExistShouldThrow() throws RepositoryException {
		assertThatThrownBy(() -> {
			museumManager.deleteExhibition(exhibition);
			when(exhibitionRepository.findExhibitionByName(EXHIBITION1_TEST)).thenReturn(null);
			doThrow(new NoSuchElementException("The selected exhibition does not exist!")).doNothing();

		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to delete Exhibition.");

	}

	@Test
	public void testDeleteExhibition() throws RepositoryException {
		when(exhibitionRepository.findExhibitionByName(EXHIBITION1_TEST)).thenReturn(exhibition);
		museumManager.deleteExhibition(exhibition);
		inOrder.verify(exhibitionRepository).findExhibitionByName(EXHIBITION1_TEST);
		inOrder.verify(exhibitionRepository).deleteExhibition(exhibition);
		verifyNoMoreInteractions(exhibitionRepository);
	}

	/**
	 * 
	 * Utility methods
	 * 
	 */

	public Museum createTestMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}

	public Exhibition createExhibition(String exhibitionName, int numOfSeats) {
		return new Exhibition(exhibitionName, numOfSeats);

	}

}
