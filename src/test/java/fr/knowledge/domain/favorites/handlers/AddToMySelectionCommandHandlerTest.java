package fr.knowledge.domain.favorites.handlers;

import fr.knowledge.domain.common.utils.IdGenerator;
import fr.knowledge.domain.common.valueobjects.Id;
import fr.knowledge.domain.common.valueobjects.Username;
import fr.knowledge.domain.favorites.SelectionType;
import fr.knowledge.domain.favorites.aggregates.Selection;
import fr.knowledge.domain.favorites.commands.AddToMySelectionCommand;
import fr.knowledge.domain.favorites.events.SelectionCreatedEvent;
import fr.knowledge.domain.favorites.exceptions.AlreadyExistingSelectionException;
import fr.knowledge.domain.favorites.ports.SelectionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AddToMySelectionCommandHandlerTest {
  @Mock
  private SelectionRepository selectionRepository;
  @Mock
  private IdGenerator idGenerator;
  private AddToMySelectionCommandHandler addToMySelectionCommandHandler;

  @Before
  public void setUp() throws Exception {
    given(idGenerator.generate()).willReturn("aaa");
    given(selectionRepository.get(Username.from("john@doe.fr"), SelectionType.CATEGORY, Id.of("aaa"))).willReturn(Optional.empty());
    addToMySelectionCommandHandler = new AddToMySelectionCommandHandler(idGenerator, selectionRepository);
  }

  @Test
  public void should_add_category_to_my_selection() throws Exception {
    AddToMySelectionCommand command = new AddToMySelectionCommand("john@doe.fr", SelectionType.CATEGORY, "aaa");

    addToMySelectionCommandHandler.handle(command);

    Selection savedSelection = Selection.of("aaa", "john@doe.fr", SelectionType.CATEGORY, "aaa");
    savedSelection.saveChanges(new SelectionCreatedEvent(Id.of("aaa"), Username.from("john@doe.fr"), SelectionType.CATEGORY, Id.of("aaa")));
    verify(selectionRepository).save(savedSelection);
  }

  @Test
  public void should_throw_exception_if_selection_already_exists() throws Exception {
    given(selectionRepository.get(Username.from("john@doe.fr"), SelectionType.CATEGORY, Id.of("aaa"))).willReturn(Optional.of(Selection.of("aaa", "john@doe.fr", SelectionType.CATEGORY, "aaa")));
    AddToMySelectionCommand command = new AddToMySelectionCommand("john@doe.fr", SelectionType.CATEGORY, "aaa");

    try {
      addToMySelectionCommandHandler.handle(command);
      fail("Should have throw AlreadyExistingSelectionException.");
    } catch (AlreadyExistingSelectionException e) {
      assertThat(e).isNotNull();
    }
  }
}