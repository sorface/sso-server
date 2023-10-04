package by.sorface.ssoserver.dao.models;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "C_ID")
    private UUID id;

    @CreatedDate
    @Column(name = "C_CREATEDDDATE")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "C_MODIFIEDDATE")
    private LocalDateTime modifiedDate;

}
