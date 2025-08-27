package ru.yandex.practicum.compilation.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.event.model.Event;

import java.util.List;

@Data
@Builder
@Entity
@Table(name = "compilations")
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(name = "compilation_events", joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events;

    @Column(nullable = false)
    private Boolean pinned = false;

    @Column(nullable = false)
    private String title;
}