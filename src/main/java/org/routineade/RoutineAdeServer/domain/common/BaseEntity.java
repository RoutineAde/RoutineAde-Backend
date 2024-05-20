package org.routineade.RoutineAdeServer.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false)
    protected String createdDate;

    @PrePersist
    public void onPrePersist() {
        this.createdDate = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.forLanguageTag("ko")));
    }
}
