package ru.yandex.practicum.event.model;

import jakarta.persistence.*;
import lombok.*;
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
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String annotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column
    private LocalDateTime createdOn;

    @Column
    private String description;

    @Column
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column
    private Boolean paid = false;

    @Column(nullable = false)
    private int participantLimit = 0;

    @Column
    private LocalDateTime publishedOn;

    @Column
    private Boolean requestModeration = true;

    @Column
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<Request> requests;

    @Column(nullable = false)
    private Long views = 0L;
}
