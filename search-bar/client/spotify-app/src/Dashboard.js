import React, {useState, useEffect} from 'react'
import useAuth from './useAuth'
import { Container, Form} from 'react-bootstrap'
import SpotifyWebApi from "spotify-web-api-node"
import TrackSearchResult from "./TrackSearchResult";
import Player from "./Player";
import axios from 'axios'
import Recommendations from "./Recommendations";

const spotifyApi = new SpotifyWebApi({
    clientId: "786d7d05062645e09e6cc0f9df174325",
})

export default function Dashboard({ code }) {
    const accessToken = useAuth(code)
    const [search, setSearch] = useState("")
    const [searchResults, setSearchResults] = useState([])
    const [playingTrack, setPlayingTrack] = useState()
    const [lyrics, setLyrics] = useState("")
    let searchArray = [];

    function chooseTrack(track) {
        setPlayingTrack(track)
        setSearch("")
        setLyrics("")
    }

    useEffect(() => {
        if (!playingTrack) return
        axios
            .get("http://localhost:3001/lyrics", {
            params: {
                track: playingTrack.title,
                artist: playingTrack.artist,
            },
        })
        .then(res => {
         setLyrics(res.data.lyrics)
        })
    }, [playingTrack])

    useEffect(() => {
        if (!accessToken) return
        spotifyApi.setAccessToken(accessToken)
    }, [accessToken])

    useEffect(() => {
        if (!search) return setSearchResults([])
        if (!accessToken) return
        let cancel = false;
        spotifyApi.searchTracks(search).then(res => {
            if (cancel) return
            searchArray = res.body.tracks.items;
            console.log("search array: " + searchArray)
            setSearchResults(res.body.tracks.items.map(track => {
                const smallestAlbumImage = track.album.images.reduce((smallest, image) => {
                    if (image.height < smallest.height) return image
                        return smallest
                }, track.album.images[0])
                return {
                    artist: track.artists[0].name,
                    title: track.name,
                    uri: track.uri,
                    albumUrl: smallestAlbumImage.url
                }
            }))
        })

        return() => cancel = true
    }, [search, accessToken])

    return (
        <Container className="d-flex flex-column py-2" style={{height: "100vh"}}>
            <Form.Control
                type="search"
                placeholder="Search Songs/Albums"
                value={search}
                onChange={e => setSearch(e.target.value)}
            />
            <div className="flex-grow-1 my-2" style={{overflowY: "auto"}}>
                {searchResults.map(track => (
                    <TrackSearchResult
                        track={track}
                        key={track.uri}
                        chooseTrack={chooseTrack}
                    />
                ))}
                {searchResults.length === 0 && (
                    <React.Fragment>
                        <div>
                            <Recommendations searchArray={searchArray}/>
                        </div>
                        <div className="text-center" style={{whiteSpace: "pre"}}>
                            <h3> Lyrics </h3>
                            {lyrics}
                        </div>
                    </React.Fragment>
                )}

            </div>
            <div>
                <Player accessToken={accessToken} trackUri={playingTrack?.uri}/>
            </div>
        </Container>
    )
}


//// Get an artist's top tracks
// spotifyApi.getArtistTopTracks('0oSGxfWSnnOXhD2fKuz2Gy', 'GB')
//   .then(function(data) {
//     console.log(data.body);
//     }, function(err) {
//     console.log('Something went wrong!', err);
//   });