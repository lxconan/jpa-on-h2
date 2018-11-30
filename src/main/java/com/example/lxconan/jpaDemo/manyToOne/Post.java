package com.example.lxconan.jpaDemo.manyToOne;

import javax.persistence.*;

@Entity
@Table(name = "post")
class Post {
    @Id
    private Long id;
    private String title;

    protected Post() {
    }

    public Post(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Post)) return false;
        final Post other = (Post) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 42;
    }
}