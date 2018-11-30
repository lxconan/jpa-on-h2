package com.example.lxconan.jpaDemo.manyToOne;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import static com.example.lxconan.jpaDemo.JpaHelper.prepareDataAndFlush;
import static com.example.lxconan.jpaDemo.JpaHelper.runAndClear;
import static com.example.lxconan.jpaDemo.JpaHelper.runJpa;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ManyToOneTests {
    @Autowired
    private EntityManager entityManager;

    @Test
    public void should_persist_post_commend_with_existing_post() {
        prepareDataAndFlush(entityManager, em -> {
            final Post post = new Post(1L, "title");
            em.persist(post);
        });

        // When
        runAndClear(entityManager, em -> {
            final Post post = em.find(Post.class, 1L);
            final PostComment comment = new PostComment(1L, "review");
            comment.setPost(post);
            em.persist(comment);
        });

        // Then
        runJpa(entityManager, em -> {
            final PostComment postComment = entityManager.find(PostComment.class, 1L);
            final Post post = entityManager.find(Post.class, 1L);

            assertThat(postComment.getReview())
                .isEqualTo("review");
            assertThat(postComment.getPost())
                .isNotNull()
                .isEqualTo(post);
        });
    }
}