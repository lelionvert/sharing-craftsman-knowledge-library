package fr.knowledge.command.api.library;

import fr.knowledge.command.api.common.AuthorizationInfoDTO;
import fr.knowledge.command.api.common.AuthorizationService;
import fr.knowledge.command.bus.CommandBus;
import fr.knowledge.domain.library.commands.*;
import fr.knowledge.domain.library.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {
  private final CommandBus commandBus;
  private final AuthorizationService authorizationService;

  @Autowired
  public LibraryService(AuthorizationService authorizationService, CommandBus commandBus) {
    this.authorizationService = authorizationService;
    this.commandBus = commandBus;
  }

  public ResponseEntity createCategory(AuthorizationInfoDTO authorizationInfoDTO, CategoryDTO categoryDTO) {
    if (!authorizationService.isUserAuthorized(authorizationInfoDTO))
      return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

    CreateCategoryCommand command = new CreateCategoryCommand(categoryDTO.getName());
    try {
      commandBus.send(command);
    } catch (AlreadyExistingCategoryException e) {
      return ResponseEntity.badRequest().body("Category already exists.");
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok().build();
  }

  public ResponseEntity updateCategory(AuthorizationInfoDTO authorizationInfoDTO, CategoryDTO categoryDTO) {
    if (!authorizationService.isUserAuthorized(authorizationInfoDTO))
      return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

    UpdateCategoryCommand command = new UpdateCategoryCommand(categoryDTO.getId(), categoryDTO.getName());
    try {
      commandBus.send(command);
    } catch (CategoryNotFoundException e) {
      return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    } catch (CategoryException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok().build();
  }

  public ResponseEntity deleteCategory(AuthorizationInfoDTO authorizationInfoDTO, String categoryId) {
    if (!authorizationService.isUserAuthorized(authorizationInfoDTO))
      return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

    DeleteCategoryCommand command = new DeleteCategoryCommand(categoryId);
    try {
      commandBus.send(command);
    } catch (CategoryNotFoundException e) {
      return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok().build();
  }

  public ResponseEntity addKnowledge(AuthorizationInfoDTO authorizationInfoDTO, KnowledgeDTO knowledgeDTO) {
    if (!authorizationService.isUserAuthorized(authorizationInfoDTO))
      return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

    AddKnowledgeCommand command = new AddKnowledgeCommand(knowledgeDTO.getCategoryId(), knowledgeDTO.getCreator(), knowledgeDTO.getTitle(), knowledgeDTO.getContent());
    try {
      commandBus.send(command);
    } catch (CategoryNotFoundException e) {
      return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    } catch (AddKnowledgeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok().build();
  }

  public ResponseEntity updateKnowledge(AuthorizationInfoDTO authorizationInfoDTO, KnowledgeDTO knowledgeDTO) {
    if (!authorizationService.isUserAuthorized(authorizationInfoDTO))
      return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

    UpdateKnowledgeCommand command = new UpdateKnowledgeCommand(knowledgeDTO.getCategoryId(), knowledgeDTO.getId(), knowledgeDTO.getCreator(), knowledgeDTO.getTitle(), knowledgeDTO.getContent());
    try {
      commandBus.send(command);
    } catch (CategoryNotFoundException | KnowledgeNotFoundException e){
      return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    } catch(AddKnowledgeException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    } catch(Exception e){
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok().build();
  }

  public ResponseEntity deleteKnowledge(AuthorizationInfoDTO authorizationInfoDTO, KnowledgeDTO knowledgeDTO) {
    if (!authorizationService.isUserAuthorized(authorizationInfoDTO))
      return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

    DeleteKnowledgeCommand command = new DeleteKnowledgeCommand(knowledgeDTO.getCategoryId(), knowledgeDTO.getId());
    try {
      commandBus.send(command);
    } catch (CategoryNotFoundException | KnowledgeNotFoundException e) {
      return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    } catch(Exception e){
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok().build();
  }
}
