package io.valentinsoare.bloggingengineapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "author")
@Table(name = "author", schema = "news_outlet_db")
@NamedEntityGraph(
        name = "author-with-posts",
        attributeNodes = @NamedAttributeNode("allPosts")
)
public class Author implements Comparable<Author> {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToMany(mappedBy = "authors")
    private Set<Post> allPosts = new HashSet<>();

    public Author setId(Long id) {
        this.id = id;
        return this;
    }

    public Author setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Author setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Author setEmail(String email) {
        this.email = email;
        return this;
    }

    public Author setAllPosts(Set<Post> allPosts) {
        this.allPosts = allPosts;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;
        return  Objects.equals(email, author.email) &&
                Objects.equals(firstName, author.firstName) &&
                Objects.equals(lastName, author.lastName);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + (firstName == null ? 0 : firstName.hashCode());
        hash = 31 * hash + (lastName == null ? 0 : lastName.hashCode());
        hash = 31 * hash + (email == null ? 0 : email.hashCode());

        return hash;
    }

    @Override
    public int compareTo(Author o) {
        return o.compareTo(this);
    }

    @Override
    public String toString() {
        return "Author [" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ']';
    }
}
