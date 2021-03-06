package fr.knowledge.domain.common.valueobjects;

public class Content {
  private final String content;

  private Content(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  public boolean isEmpty() {
    return content.isEmpty();
  }

  public static Content of(String content) {
    return new Content(content);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Content content1 = (Content) o;

    return content != null ? content.equals(content1.content) : content1.content == null;
  }

  @Override
  public int hashCode() {
    return content != null ? content.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Content{" +
            "content='" + content + '\'' +
            '}';
  }
}
