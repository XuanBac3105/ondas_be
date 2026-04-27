package com.example.ondas_be.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeResponse {

    private List<SongResponse> trendingSongs;
    private List<ArtistResponse> featuredArtists;
    private List<AlbumResponse> newReleases;
}
