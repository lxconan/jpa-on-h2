package com.example.lxconan.jpaDemo.manyToOne;

import com.example.lxconan.jpaDemo.JpaTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ManyToOneWithPersistCascadingTests extends JpaTestBase {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCommentPersistRepository postCommentPersistRepository;

    @Test
    public void should_be_able_to_persist_post_when_persist_comment_at_the_same_time() {
        // When
        flushAndClear(em -> {
            final Post post = new Post(1L, "post");
            final PostCommentPersist comment = new PostCommentPersist(1L, "comment");
            comment.setPost(post);
            em.persist(comment);
        });

        // Then
        runJpa(em -> {
            final PostCommentPersist comment = postCommentPersistRepository.findOneById(1L);
            final Post post = postRepository.findOneById(1L);
            assertThat(comment.getPost()).isEqualTo(post);
        });
    }

    @Test
    public void should_not_be_able_to_persist_post_when_save_comment_at_the_same_time_by_repository() {
        // Given
        final Post post = new Post(1L, "post");
        final PostCommentPersist comment = new PostCommentPersist(1L, "comment");
        comment.setPost(post);

        // When-Then
        assertThatExceptionOfType(JpaObjectRetrievalFailureException.class).isThrownBy(() -> {
            // This gonna be very interesting because if the ID is not null for non-primitive type, the JPA
            // will treat the entity as NOT NEW. Thus to call merge() rather than persist(). The merge()
            // will try to retrieve information first, which cause the failure.
            postCommentPersistRepository.save(comment);
            postCommentPersistRepository.flush();
        });
    }
}
