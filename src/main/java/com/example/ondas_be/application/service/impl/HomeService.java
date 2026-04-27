package com.example.ondas_be.application.service.impl;

import com.example.ondas_be.application.dto.response.AlbumResponse;
import com.example.ondas_be.application.dto.response.ArtistResponse;
import com.example.ondas_be.application.dto.response.ArtistSummaryResponse;
import com.example.ondas_be.application.dto.response.GenreSummaryResponse;
import com.example.ondas_be.application.dto.response.HomeResponse;
import com.example.ondas_be.application.dto.response.SongResponse;
import com.example.ondas_be.application.mapper.AlbumMapper;
import com.example.ondas_be.application.mapper.ArtistMapper;
import com.example.ondas_be.application.mapper.GenreMapper;
import com.example.ondas_be.application.mapper.SongMapper;
import com.example.ondas_be.application.service.port.HomeServicePort;
import com.example.ondas_be.domain.entity.Artist;
import com.example.ondas_be.domain.entity.Genre;
import com.example.ondas_be.domain.entity.Song;
import com.example.ondas_be.domain.repoport.AlbumArtistRepoPort;
import com.example.ondas_be.domain.repoport.AlbumRepoPort;
import com.example.ondas_be.domain.repoport.ArtistRepoPort;
import com.example.ondas_be.domain.repoport.GenreRepoPort;
import com.example.ondas_be.domain.repoport.SongArtistRepoPort;
import com.example.ondas_be.domain.repoport.SongGenreRepoPort;
import com.example.ondas_be.domain.repoport.SongRepoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeService implements HomeServicePort {

    private final SongRepoPort songRepoPort;
    private final ArtistRepoPort artistRepoPort;
    private final AlbumRepoPort albumRepoPort;
    private final GenreRepoPort genreRepoPort;
    private final SongArtistRepoPort songArtistRepoPort;
    private final SongGenreRepoPort songGenreRepoPort;
    private final AlbumArtistRepoPort albumArtistRepoPort;
    private final SongMapper songMapper;
    private final ArtistMapper artistMapper;
    private final AlbumMapper albumMapper;
    private final GenreMapper genreMapper;

    @Override
    @Transactional(readOnly = true)
    public HomeResponse getHome(int trendingLimit, int artistLimit, int albumLimit) {
        return HomeResponse.builder()
                .trendingSongs(buildTrendingSongs(trendingLimit))
                .featuredArtists(buildFeaturedArtists(artistLimit))
                .newReleases(buildNewReleases(albumLimit))
                .build();
    }

    private List<SongResponse> buildTrendingSongs(int limit) {
        List<Song> songs = songRepoPort.findTopByPlayCount(limit);
        if (songs.isEmpty()) {
            return Collections.emptyList();
        }

        List<UUID> songIds = songs.stream().map(Song::getId).toList();

        Map<UUID, List<UUID>> artistIdsBySong = songArtistRepoPort.findArtistIdsBySongIds(songIds);
        Map<UUID, List<Long>> genreIdsBySong = songGenreRepoPort.findGenreIdsBySongIds(songIds);

        List<UUID> allArtistIds = artistIdsBySong.values().stream()
                .flatMap(List::stream).distinct().toList();
        List<Long> allGenreIds = genreIdsBySong.values().stream()
                .flatMap(List::stream).distinct().toList();

        Map<UUID, ArtistSummaryResponse> artistById = artistRepoPort.findByIds(allArtistIds).stream()
                .collect(Collectors.toMap(Artist::getId, artistMapper::toSummaryResponse));
        Map<Long, GenreSummaryResponse> genreById = genreRepoPort.findByIds(allGenreIds).stream()
                .collect(Collectors.toMap(Genre::getId, genreMapper::toSummaryResponse));

        return songs.stream().map(song -> {
            SongResponse response = songMapper.toResponse(song);
            response.setArtists(artistIdsBySong.getOrDefault(song.getId(), Collections.emptyList())
                    .stream().map(artistById::get).filter(Objects::nonNull).toList());
            response.setGenres(genreIdsBySong.getOrDefault(song.getId(), Collections.emptyList())
                    .stream().map(genreById::get).filter(Objects::nonNull).toList());
            return response;
        }).toList();
    }

    private List<ArtistResponse> buildFeaturedArtists(int limit) {
        return artistMapper.toResponseList(artistRepoPort.findAll(0, limit));
    }

    private List<AlbumResponse> buildNewReleases(int limit) {
        return albumRepoPort.findLatestReleases(limit).stream().map(album -> {
            AlbumResponse response = albumMapper.toResponse(album);
            response.setArtistIds(albumArtistRepoPort.findArtistIdsByAlbumId(album.getId()));
            response.setTracklist(Collections.emptyList());
            return response;
        }).toList();
    }
}
