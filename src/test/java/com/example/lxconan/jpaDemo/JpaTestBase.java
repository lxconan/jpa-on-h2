package com.example.lxconan.jpaDemo;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.function.Consumer;

public abstract class JpaTestBase {
    @Autowired
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {return entityManager;}

    protected void flush(Consumer<EntityManager> consumer) {
        final EntityManager em = getEntityManager();
        consumer.accept(em);
        em.flush();
    }

    public void flushAndClear(Consumer<EntityManager> consumer) {
        final EntityManager em = getEntityManager();
        consumer.accept(em);
        em.flush();
        em.clear();
    }

    public void runJpa(Consumer<EntityManager> consumer) {
        final EntityManager em = getEntityManager();
        consumer.accept(em);
    }
}
