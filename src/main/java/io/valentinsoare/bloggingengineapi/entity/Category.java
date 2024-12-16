package io.valentinsoare.bloggingengineapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "category")
@Table(name = "category", schema = "news_outlet_db")
@NamedEntityGraph(
        name = "category-with-posts",
        attributeNodes = @NamedAttributeNode("posts")
)
public class Category implements Comparable<Category> {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany(mappedBy = "categories")
    private Set<Post> posts = new HashSet<>();

    public Category setId(Long id) {
        this.id = id;
        return this;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public Category setDescription(String description) {
        this.description = description;
        return this;
    }

    public Category setPosts(Set<Post> posts) {
        this.posts = posts;
        return this;
    }

    public void addPost(Post post) {
        posts.add(post);
        post.getCategories().add(this);
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.getCategories().remove(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;
        return Objects.equals(name, category.name) &&
                Objects.equals(description, category.description);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + (name == null ? 0 : name.hashCode());
        hash = 31 * hash + (description == null ? 0 : description.hashCode());

        return hash;
    }

    @Override
    public int compareTo(Category o) {
        if (o == null) return -2;
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "Category [" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ']';
    }
}
