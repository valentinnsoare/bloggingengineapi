package io.valentinsoare.bloggingengineapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Comparator;
import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "comment")
@Table(name = "comment", schema = "news_outlet_db")
public class Comment implements Comparable<Comment> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @JdbcTypeCode(SqlTypes.CLOB)
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "body", nullable = false)
    private String body;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Comment setId(@NotNull  Long id) {
        this.id = id;
        return this;
    }

    public Comment setBody(@Size(max = 5000) @NotBlank(message = "Comment body is mandatory!") String body) {
        this.body = body;
        return this;
    }

    public Comment setEmail(@Size(max = 50) @NotBlank(message = "Email is mandatory!") String email) {
        this.email = email;
        return this;
    }

    public Comment setName(@Size(max = 25) @NotBlank(message = "Name is mandatory!") String name) {
        this.name = name;
        return this;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;
        return Objects.equals(body, comment.body) &&
                Objects.equals(email, comment.email)
                && Objects.equals(post, comment.post);
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 31 * hash + (body == null ? 0 : body.hashCode());
        hash = 31 * hash + (email == null ? 0 : email.hashCode());
        hash = 31 * hash + (post == null ? 0 : post.hashCode());

        return hash;
    }

    @Override
    public int compareTo(@NonNull  Comment o) {
        return Comparator.comparing((Comment c) -> c.name)
                .thenComparing(c -> c.email)
                .thenComparing(c -> c.body)
                .thenComparing(c -> c.post)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return "Comment [" +
                "id=" + id +
                ']';
    }
}
