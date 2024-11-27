package io.hhplus.reserve.outbox.infra;

import io.hhplus.reserve.outbox.domain.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxJpaRepository extends JpaRepository<Outbox, String> {

    @Query("select o from Outbox o where o.isPublished = :isPublished and o.count <= :count")
    List<Outbox> findAllByPublished(@Param("isPublished") boolean isPublished, @Param("count") int count);
}
