package tilacademy.attendance.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "packages", indexes = {
        @Index(name = "idx_packages_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pkg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название пакета, например "Monthly 12" */
    @Column(nullable = false, length = 150, unique = true)
    private String name;

    /** Описание пакета (опционально) */
    @Column(columnDefinition = "text")
    private String description;

    /** Количество уроков в пакете (12, 24 и т.д.) */
    @Column(nullable = false)
    private Integer lessonsCount;

    /** Длительность пакета в днях (обычно 30) */
    @Column(nullable = false)
    private Integer durationDays;

    /** Цена пакета */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /** Активность пакета — менеджер видит только активные пакеты */
    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;
}
