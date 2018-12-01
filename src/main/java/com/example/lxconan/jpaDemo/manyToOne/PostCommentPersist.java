package com.example.lxconan.jpaDemo.manyToOne;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;

@Entity
@Table(name = "post_comment_persist")
class PostCommentPersist {
    @Id
    private Long id;
    private String review;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "post_id")
    private Post post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    protected PostCommentPersist() {}

    public PostCommentPersist(Long id, String review) {
        this.id = id;
        this.review = review;
    }
}

interface PostCommentPersistRepository extends JpaRepository<PostCommentPersist, Long> {
    PostCommentPersist findOneById(Long id);
}