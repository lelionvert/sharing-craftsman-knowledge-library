package fr.knowledge.domain.library.valueobjects;

public class Name {
  private final String name;

  private Name(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public boolean isEmpty() {
    return name.isEmpty();
  }

  public static Name of(String name) {
    return new Name(name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Name name1 = (Name) o;

    return name != null ? name.equals(name1.name) : name1.name == null;
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Name{" +
            "name='" + name + '\'' +
            '}';
  }
}
