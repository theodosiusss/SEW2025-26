export interface SongInterface {
    id: number;
    title: string;
    artist: ArtistInterface
    length: string;
    genre: string;
}

export interface ArtistInterface {
    id: number;
    name: string;
    version: string
}