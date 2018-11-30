package com.example.lxconan.jpaDemo;

import javax.persistence.EntityManager;
import java.util.function.Consumer;

public class JpaHelper {
    public static void prepareDataAndFlush(EntityManager em, Consumer<EntityManager> consumer) {
        consumer.accept(em);
        em.flush();
    }

    public static void runAndClear(EntityManager em, Consumer<EntityManager> consumer) {
        consumer.accept(em);
        em.flush();
        em.clear();
    }

    public static void runJpa(EntityManager em, Consumer<EntityManager> consumer) {
        consumer.accept(em);
    }
}
