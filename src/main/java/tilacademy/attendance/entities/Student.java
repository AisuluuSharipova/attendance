package tilacademy.attendance.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "students", indexes = {
        @Index(name = "idx_students_email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String email;

    private String phone;

    @Column(columnDefinition = "text")
    private String notes;

    /**
     * Привязанный пакет (Admin создаёт пакеты; менеджер назначает пакет студенту).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private Pkg pkg;

    /**
     * Сколько уроков осталось у студента. Каждый проведённый урок уменьшает это поле на 1.
     * При назначении пакета менеджер обычно устанавливает lessonsRemaining = pkg.lessonsCount.
     */

    @Builder.Default
    @Column(nullable = false)
    private Integer lessonsRemaining = 0;

    /** createdAt сохраняется только для Student */
    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }

    /**
     * Транзиентный геттер — не сохраняется в БД.
     * Используется фронтом/DTO для отображения остатка уроков.
     */
    @Transient
    public Integer getRemainingLessons() {
        return (this.lessonsRemaining == null) ? 0 : this.lessonsRemaining;
    }

    /**
     * UI-маркер: подсветка (например красным), когда осталось мало уроков (threshold = 2).
     * Это транзиентный метод — фронт может сам подсвечивать строку на его основе.
     */
    @Transient
    public boolean isLowOnLessons() {
        Integer rem = getRemainingLessons();
        return rem <= 2;
    }

    @Transient
    public String getFullName() {
        String f = firstName == null ? "" : firstName;
        String l = lastName == null ? "" : lastName;
        return (f + " " + l).trim();
    }
}

