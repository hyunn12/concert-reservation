package io.hhplus.reserve.outbox.domain;

import io.hhplus.reserve.support.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "outbox")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Outbox extends BaseEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "domain_name")
    private String domainName;

    @Column(name = "topic", columnDefinition = "longtext")
    private String topic;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "message")
    private String message;

    @Builder.Default
    @Column(name = "is_published", columnDefinition = "tinyint")
    private boolean isPublished = false;

    @Column(name = "count")
    private int count;

    public static Outbox create(String id, String domainName, String topic, String eventType, String message) {
        return Outbox.builder()
                .id(id)
                .domainName(domainName)
                .topic(topic)
                .eventType(eventType)
                .message(message)
                .count(0)
                .build();
    }

    public void published() {
        this.isPublished = true;
    }

    public void unpublished() {
        this.isPublished = false;
    }

    public void increaseCount() {
        this.count++;
    }

}
