package io.valentinsoare.bloggingengineapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "post")
@Table(name = "post", schema = "news_outlet_db")
@NamedEntityGraph(
        name = "post-with-authors-categories-comments",
        attributeNodes = {
                @NamedAttributeNode("categories"),
                @NamedAttributeNode("authors"),
                @NamedAttributeNode("comments")
        }
)
public class Post implements Comparable<Post> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "post_category",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private Set<Category> categories = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "post_author",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id")
    )
    private Set<Author> authors = new HashSet<>();

    @JdbcTypeCode(SqlTypes.CLOB)
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "content", nullable = false)
    private String content;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    public Post setId(@NotNull  Long id) {
        this.id = id;
        return this;
    }

    public Post setCategories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }

    public Post setComments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Post setTitle(@Size(max = 150) @NotBlank(message = "Title is mandatory!") String title) {
        this.title = title;
        return this;
    }

    public Post setDescription(@Size(max = 255) @NotBlank(message = "Description is mandatory!") String description) {
        this.description = description;
        return this;
    }

    public Post setContent(@Size(max = 15000) @NotBlank(message = "Content is mandatory!") String content) {
        this.content = content;
        return this;
    }

    public Post setAuthors(Set<Author> authors) {
        this.authors = authors;
        return this;
    }

    public void addComment(@NotNull  Comment comment) {
        comment.setPost(this);
        comments.add(comment);
    }

    public void removeComment(@NotNull  Comment comment) {
        comment.setPost(null);
        comments.remove(comment);
    }

    public void addCategory(Category category) {
        this.categories.add(category);
        category.getPosts().add(this);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.getPosts().remove(this);
    }

    public void addAuthor(Author author) {
        this.authors.add(author);
        author.getAllPosts().add(this);
    }

    public void removeAuthor(Author author) {
        this.authors.remove(author);
        author.getAllPosts().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;
        return Objects.equals(title, post.title) && Objects.equals(content, post.content);
    }

    @Override
    public int hashCode() {
        int hash = 11;

        hash = 31 * hash + (title == null ? 0 : title.hashCode());
        hash = 31 * hash + (content == null ? 0 : content.hashCode());

        return hash;
    }

    @Override
    public int compareTo(@NonNull Post o) {
        return Comparator.comparing((Post p) -> p.title)
                .thenComparing(p -> o.getAuthors().removeAll(authors) ? 0 : 1)
                .thenComparing(p -> description)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return "Post [" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", categories=" + categories +
                ", authors=" + authors +
                ']';
    }
}
