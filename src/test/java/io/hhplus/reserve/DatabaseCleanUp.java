package io.hhplus.reserve;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@Component
@ActiveProfiles("test")
public class DatabaseCleanUp implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tables = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        List<EntityType<?>> entityTypes = entityManager.getMetamodel()
                .getEntities()
                .stream()
                .filter(entity -> entity.getJavaType().getAnnotation(Entity.class) != null)
                .toList();

        for (EntityType<?> entityType : entityTypes) {
            Table tableAnnotation = entityType.getJavaType().getAnnotation(Table.class);
            if (tableAnnotation != null) {
                tables.add(tableAnnotation.name());
            }
        }
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (String table : tables) {
            entityManager.createNativeQuery(String.format("TRUNCATE TABLE %s", table)).executeUpdate();
            entityManager.createNativeQuery(String.format("ALTER TABLE %s ALTER COLUMN %s_id RESTART WITH 1", table, table)).executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

}
