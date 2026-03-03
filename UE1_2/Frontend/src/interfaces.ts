export interface SongInterface {
    id: number;
    title: string;
    artist: ArtistInterface
    length: string;
    genres: string[];
    user: {
        username: string;
    }
}

export interface ArtistInterface {
    id: number;
    name: string;
    version: string
}