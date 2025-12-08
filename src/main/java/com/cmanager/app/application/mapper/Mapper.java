package com.cmanager.app.application.mapper;

import com.cmanager.app.application.data.ShowDto;
import com.cmanager.app.application.domain.EpisodeLinks;
import com.cmanager.app.application.domain.Episodes;
import com.cmanager.app.application.domain.Rating;
import com.cmanager.app.application.domain.Show;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class Mapper {

    public static Show toEntity(ShowDto dto) {
        if (dto == null) {
            return null;
        }
        Show entity = new Show();

        entity.setId(dto.id() != null ? dto.id().toString() : null);
        entity.setIdIntegration(dto.id() != null ? dto.id().intValue() : null);
        entity.setName(dto.name());
        entity.setType(dto.type());
        entity.setLanguage(dto.language());
        entity.setStatus(dto.status());
        entity.setRuntime(dto.runtime());
        entity.setAverageRuntime(dto.averageRuntime());
        entity.setOfficialSite(dto.officialSite());

        if (dto.rating() != null && dto.rating().average() != null) {
            entity.setRating(BigDecimal.valueOf(dto.rating().average()));
        } else {
            entity.setRating(BigDecimal.ZERO);
        }
        entity.setSummary(dto.summary());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());

        return entity;
    }

    public static Episodes toEntity(ShowDto.Embedded.Episode episode) {
        if (episode == null) {
            return null;
        }

        Episodes entity = new Episodes();
        entity.setId(episode.id());
        entity.setUrl(episode.url());
        entity.setName(episode.name());
        entity.setSeason(episode.season());
        entity.setNumber(episode.number());
        entity.setType(episode.type());
        entity.setAirdate(episode.airdate());
        entity.setAirtime(episode.airtime());
        entity.setAirstamp(episode.airstamp());
        entity.setRuntime(episode.runtime());
        entity.setSummary(episode.summary());

        if (episode.rating() != null) {
            if (entity.getRating() == null) {
                entity.setRating(new Rating());
            }
            entity.getRating().setAverage(episode.rating().average());
        }

        if (episode.image() != null) {
            entity.setImage(episode.image().original());
        } else {
            entity.setImage(null);
        }

        if (episode._links() != null) {
            EpisodeLinks links = new EpisodeLinks();

            if (episode._links().self() != null) {
                EpisodeLinks.Self self = new EpisodeLinks.Self();
                self.setHref(episode._links().self().href());
                links.setSelf(self);
            }

            if (episode._links().show() != null) {
                EpisodeLinks.Show show = new EpisodeLinks.Show();
                show.setHref(episode._links().show().href());
                show.setName(episode._links().show().name());
                links.setShow(show);
            }
            entity.setLinks(links);
        }
        return entity;
    }
    public static List<Episodes> toEntityList(List<ShowDto.Embedded.Episode> episodes) {
        if (episodes == null) {
            return List.of();
        }
        return episodes.stream()
                .map(Mapper::toEntity)
                .toList();
    }
}


