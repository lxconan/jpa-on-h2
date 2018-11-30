package com.example.lxconan.jpaDemo.manyToOne;

import javax.persistence.*;

@Entity
@Table(name = "post_comment")
class PostComment {
    @Id
    private Long id;
    private String review;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    protected PostComment() { }

    public PostComment(Long id, String review) {
        this.id = id;
        this.review = review;
    }

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
}
