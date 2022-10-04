package io.dsub.discogs.api.artist.query;

public class ArtistQuery {
    public record ByID(int id){}
    public record ByName(String name){}
}
