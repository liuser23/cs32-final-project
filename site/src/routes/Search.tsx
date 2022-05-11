import React, {useState, useEffect, Dispatch, SetStateAction} from 'react'
import { Container, Form} from 'react-bootstrap'
import TrackSearchResult from "./TrackSearchResult";
import axios from 'axios'
import {Authentication} from "../App";
import {track} from "../MyTypes";

type SearchResult = {
    artist: string,
    name: string,
    image: string | undefined,
    spotifyId: string,
}

function Search(props: {authentication: Authentication, setNowPlaying: Dispatch<SetStateAction<string | undefined>>}) {
    const [query, setQuery] = useState<string>('')
    const [searchResults, setSearchResults] = useState<SearchResult[]>([])

    useEffect(() => {
        let cancel = false
        if (!query) return

        const config = {
            headers: {
                'Authentication': props.authentication.sessionToken,
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            },
            params: {
                'query': query,
            },
        }

        axios.get(process.env.REACT_APP_SEARCH_ENDPOINT as string, config)
            .then(r => {
                console.log(cancel, r)
                if (cancel) return
                const tracks = r.data as track[]
                setSearchResults(tracks.map(song => {
                    const image = song.album.images.reduce((accum, current) =>
                        accum.width*accum.height < current.width*current.height? accum : current)
                    return {
                        image: image.url,
                        artist: song.artists[0]?.name,
                        name: song.name,
                        spotifyId: song.uri,
                    }
                }))
            })
        return () => { cancel = true }
    }, [query])

    return (
        <Container className="d-flex flex-column py-2 Main-window" style={{height: "100vh"}}>
            <Form.Control
                size="lg"
                type="search"
                placeholder="Search Songs/Albums"
                value={query}
                onChange={e => setQuery(e.target.value)}
            />
            <div className="flex-grow-1 my-2" style={{overflowY: "auto"}}>
                {searchResults.map(track => (
                    <TrackSearchResult
                        key={track.spotifyId}
                        track={track}
                        setNowPlaying={props.setNowPlaying}
                    />
                ))}

            </div>
        </Container>
    )
}

export type {SearchResult}
export default Search;

//// Get an artist's top tracks
// spotifyApi.getArtistTopTracks('0oSGxfWSnnOXhD2fKuz2Gy', 'GB')
//   .then(function(data) {
//     console.log(data.body);
//     }, function(err) {
//     console.log('Something went wrong!', err);
//   });