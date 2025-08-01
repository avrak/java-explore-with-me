package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.StatisticsGetDto;
import ru.yandex.practicum.model.Statistics;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    @Query("""
        select new ru.yandex.practicum.StatisticsGetDto(
                 s.app,
                 s.uri,
                 case
                    when :unique = true then count(distinct s.ip)
                    else count(s.ip)
                 end
               )
          from Statistics s
         where s.timestamp between :start and :end
         group by s.app, s.uri
    """
    )
    List<StatisticsGetDto> findStatisticsByPeriod(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("unique") Boolean unique);

    @Query("""
        select new ru.yandex.practicum.StatisticsGetDto(
                 s.app,
                 s.uri,
                 case
                    when :unique = true then count(distinct s.ip)
                    else count(s.ip)
                 end
               )
          from Statistics s
         where s.timestamp between :start and :end
           and s.uri in :uris
         group by s.app, s.uri
    """
    )
    List<StatisticsGetDto> findStatisticsByPeriodAndUris(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris,
            @Param("unique") Boolean unique);
}
