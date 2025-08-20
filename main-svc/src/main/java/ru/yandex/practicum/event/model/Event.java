package ru.yandex.practicum.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.location.model.Location;
import ru.yandex.practicum.request.model.Request;
import ru.yandex.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Length(min = 20, max = 2000)
    @NotBlank
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column
    private Boolean paid = false;

    @Column(name = "participant_limit")
    @PositiveOrZero
    private int participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration = true;

    @Column
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column
    @NotBlank
    @Length(min = 3, max = 120)
    private String title;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<Request> requests;

    @Column(nullable = false)
    private Long views = 0L;
}
