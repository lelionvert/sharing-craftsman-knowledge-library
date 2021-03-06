package fr.knowledge.command.api.library;

import fr.knowledge.command.api.common.AuthorizationInfoDTO;
import fr.knowledge.command.api.common.AuthorizationService;
import fr.knowledge.command.bus.CommandBus;
import fr.knowledge.domain.library.commands.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LibraryServiceTest {
  @Mock
  private CommandBus commandBus;
  private LibraryService libraryService;
  private AuthorizationInfoDTO authorizationInfoDTO;
  @Mock
  private AuthorizationService authorizationService;

  @Before
  public void setUp() {
    given(authorizationService.isUserAuthorized(any(AuthorizationInfoDTO.class))).willReturn(true);

    authorizationInfoDTO = new AuthorizationInfoDTO(
            "client",
            "secret",
            "john@doe.fr",
            "token"
    );
    libraryService = new LibraryService(authorizationService, commandBus);
  }

  @Test
  public void should_create_category() throws Exception {
    ResponseEntity response = libraryService.createCategory(authorizationInfoDTO, CategoryDTO.from("Architecture"));

    CreateCategoryCommand command = new CreateCategoryCommand("Architecture");
    verify(commandBus).send(command);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_update_category() throws Exception {
    ResponseEntity response = libraryService.updateCategory(authorizationInfoDTO, CategoryDTO.from("aaa", "Architecture"));

    UpdateCategoryCommand command = new UpdateCategoryCommand("aaa", "Architecture");
    verify(commandBus).send(command);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_delete_category() throws Exception {
    ResponseEntity response = libraryService.deleteCategory(authorizationInfoDTO, "aaa");

    DeleteCategoryCommand command = new DeleteCategoryCommand("aaa");
    verify(commandBus).send(command);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_add_knowledge_to_category() throws Exception {
    ResponseEntity response = libraryService.addKnowledge(authorizationInfoDTO, KnowledgeDTO.from("aaa", "john@doe.fr", "title", "content"));

    AddKnowledgeCommand command = new AddKnowledgeCommand("aaa", "john@doe.fr", "title", "content");
    verify(commandBus).send(command);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_update_knowledge_to_category() throws Exception {
    ResponseEntity response = libraryService.updateKnowledge(authorizationInfoDTO, KnowledgeDTO.from("aaa", "aaa", "john@doe.fr", "title", "content"));

    UpdateKnowledgeCommand command = new UpdateKnowledgeCommand("aaa", "aaa", "john@doe.fr", "title", "content");
    verify(commandBus).send(command);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void should_delete_knowledge() throws Exception {
    ResponseEntity response = libraryService.deleteKnowledge(authorizationInfoDTO, KnowledgeDTO.from("aaa", "aaa"));

    DeleteKnowledgeCommand command = new DeleteKnowledgeCommand("aaa", "aaa");
    verify(commandBus).send(command);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}
