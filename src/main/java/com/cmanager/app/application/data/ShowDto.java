package com.cmanager.app.application.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "ShowDTO", description = "Response da sincronização de TV shows")
@JsonIgnoreProperties(ignoreUnknown = true)
public record ShowDto(
        Long id,
        String url,
        String name,
        String type,
        String language,
        List<String> genres,
        String status,
        Integer runtime,
        Integer averageRuntime,
        String premiered,
        String ended,
        String officialSite,
        Schedule schedule,
        Rating rating,
        Integer weight,
        Network network,
        Object webChannel,
        Object dvdCountry,
        Externals externals,
        Image image,
        String summary,
        Long updated,
        Links _links,
        Embedded _embedded
) {
    public record Schedule(String time, List<String> days) {}
    public record Rating(Double average) {}
    public record Network(Long id, String name, Country country, String officialSite) {
        public record Country(String name, String code, String timezone) {}
    }
    public record Externals(Integer tvrage, Integer thetvdb, String imdb) {}
    public record Image(String medium, String original) {}
    public record Links(Self self, PreviousEpisode previousepisode) {
        public record Self(String href) {}
        public record PreviousEpisode(String href, String name) {}
    }
    public record Embedded(List<Episode> episodes) {
        public record Episode(Long id, String url, String name, Integer season, Integer number,
                              String type, String airdate, String airtime, String airstamp,
                              Integer runtime, Rating rating, Image image, String summary,
                              EpisodeLinks _links) {
            public record EpisodeLinks(Self self, Show show) {
                public record Self(String href) {}
                public record Show(String href, String name) {}
            }
        }
    }
}