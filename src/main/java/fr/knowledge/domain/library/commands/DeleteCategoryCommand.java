package fr.knowledge.domain.library.commands;

import fr.knowledge.domain.common.valueobjects.Id;

public class DeleteCategoryCommand {
  private String id;

  public DeleteCategoryCommand(String id) {
    this.id = id;
  }

  public Id getId() {
    return Id.of(id);
  }
}