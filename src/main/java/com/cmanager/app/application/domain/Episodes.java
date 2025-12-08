package com.cmanager.app.application.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "episodes")
@Getter
@Setter
public class Episodes {

    @Id
    private Long id;

    private String url;

    @Column(nullable = false)
    private String name;

    private Integer season;
    private Integer number;
    private String type;

    private String airdate;
    private String airtime;
    private String airstamp;

    private Integer runtime;

    @Embedded
    private Rating rating;

    private String image;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Embedded
    private EpisodeLinks links;
}
