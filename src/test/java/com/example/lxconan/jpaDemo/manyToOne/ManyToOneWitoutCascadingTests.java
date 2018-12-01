package com.example.lxconan.jpaDemo.manyToOne;

import com.example.lxconan.jpaDemo.JpaTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ManyToOneWitoutCascadingTests extends JpaTestBase {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Test
    public void should_persist_post_comment_with_existing_post() {
        flush(em -> {
            final Post post = new Post(1L, "title");
            postRepository.save(post);
        });

        // When
        flushAndClear(em -> {
            final Post post = postRepository.findOneById(1L);
            final PostComment comment = new PostComment(1L, "review");
            comment.setPost(post);
            postCommentRepository.save(comment);
        });

        // Then
        runJpa(em -> {
            final PostComment postComment = postCommentRepository.findOneById(1L);
            final Post post = postRepository.findOneById(1L);

            assertThat(postComment.getReview())
                .isEqualTo("review");
            assertThat(postComment.getPost())
                .isNotNull()
                .isEqualTo(post);
        });
    }

    @Test
    public void should_not_persist_new_post_comment_alone_with_new_post() {
        // Given
        final Post post = new Post(1L, "post");
        final PostComment comment = new PostComment(1L, "comment");
        comment.setPost(post);

        // When
        runJpa(em -> {
            // To let this operation work, the cascading options should contain "PERSIST".
            assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> {
                    em.persist(comment);
                    em.flush();
                });
        });
    }

    @Test
    public void should_break_connection_between_post_comment_with_post() {
        // Given
        givenPostAndComment(1L, "post", 1L, "comment");

        // When
        flushAndClear(em -> {
            final PostComment comment = postCommentRepository.findOneById(1L);
            comment.setPost(null);
            postCommentRepository.save(comment);
        });

        // Then
        runJpa(em -> {
            final Post post = postRepository.findOneById(1L);
            final PostComment comment = postCommentRepository.findOneById(1L);
            assertThat(post)
                .isNotNull()
                .matches(p -> p.getTitle().equals("post"));
            assertThat(comment)
                .isNotNull()
                .matches(p -> p.getReview().equals("comment"))
                .matches(p -> p.getPost() == null);
        });
    }

    @Test
    public void should_not_delete_post_when_deleting_comment() {
        // Given
        givenPostAndComment(1L, "post", 1L, "comment");

        // When
        flushAndClear(em -> postCommentRepository.deleteById(1L));

        // Then
        runJpa(em -> {
            assertThat(postCommentRepository.existsById(1L)).isFalse();
            assertThat(postRepository.existsById(1L)).isTrue();
        });
    }

    @Test
    public void should_not_delete_comment_when_deleting_post() {
        // Given
        givenPostAndComment(1L, "post", 1L, "comment");

        // When
        runJpa(em -> {
            // This is based on spring.jpa.hibernate.ddl-auto=create-drop. Since the many-to-one will create a FK
            // between Post and PostComment.
            assertThatExceptionOfType(PersistenceException.class)
                .isThrownBy(() -> {
                    postRepository.deleteById(1L);
                    em.flush();
                });
        });
    }

    private void givenPostAndComment(long postId, String postTitle, long commentId, String commentReview) {
        flushAndClear(em -> {
            final Post post = new Post(postId, postTitle);
            postRepository.save(post);
            final PostComment comment = new PostComment(commentId, commentReview);
            comment.setPost(post);
            postCommentRepository.save(comment);
        });
    }

    @Test
    public void should_not_delete_post_when_deleting_post_comment() {
        // Given
        flushAndClear(em -> {

        });
    }
}