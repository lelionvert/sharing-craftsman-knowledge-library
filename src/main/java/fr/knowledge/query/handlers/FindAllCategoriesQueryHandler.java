package fr.knowledge.query.handlers;

import fr.knowledge.infra.models.library.CategoryElastic;
import fr.knowledge.query.queries.Query;

import java.util.List;

public class FindAllCategoriesQueryHandler implements QueryHandler<CategoryElastic> {
  @Override
  public List handle(Query query) {
    return null;
  }
}