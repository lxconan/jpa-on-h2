package com.example.lxconan.jpaDemo.manyToOne;

import com.example.lxconan.jpaDemo.JpaTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ManyToOneTests extends JpaTestBase {
    @Test
    public void should_persist_post_commend_with_existing_post() {
        prepareDataAndFlush(em -> {
            final Post post = new Post(1L, "title");
            em.persist(post);
        });

        // When
        runAndClear(em -> {
            final Post post = em.find(Post.class, 1L);
            final PostComment comment = new PostComment(1L, "review");
            comment.setPost(post);
            em.persist(comment);
        });

        // Then
        runJpa(em -> {
            final PostComment postComment = em.find(PostComment.class, 1L);
            final Post post = em.find(Post.class, 1L);

            assertThat(postComment.getReview())
                .isEqualTo("review");
            assertThat(postComment.getPost())
                .isNotNull()
                .isEqualTo(post);
        });
    }
}