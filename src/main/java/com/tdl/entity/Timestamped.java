package com.tdl.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) //자동으로 시간을 넣어주는 기능이 수행된다.
public abstract class Timestamped {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @CreatedDate
    @Column(updatable = false) //최초 생성시간만 초기화 되고 그 뒤 수정될 수 없음
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @LastModifiedDate //변경될 때마다 시간 저장
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt;

    public String getCreatedAtFormatted() {
        return createdAt.format(FORMATTER);
    }

    public String getModifiedAtFormatted() {
        return modifiedAt.format(FORMATTER);
    }
}