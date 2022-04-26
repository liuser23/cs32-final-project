import React, {useState, useEffect} from 'react';
import SpotifyWebApi from 'spotify-web-api-node'
// import useAuth from "./useAuth"
// import Player from "./Player"
// import TrackSearchResult from "./TrackSearchResult"
import {Container, Form} from 'react-bootstrap'
import axios from "axios"

const spotifyApi = new SpotifyWebApi({
    clientId: "8b945ef10ea24755b83ac50cede405a0",
})

// export default function Dashboard({ code }) {
//     const accessToken = useAuth(code)
//     const [search, setSearch] = useState("")
//     const [searchResults, setSearchResults] = useState([])
//     const [playingTrack, setPlayingTrack] = useState()
//     const [lyrics, setLyrics] = useState("")
//
//     function chooseTrack(track) {
//         setPlayingTrack(track)
//         setSearch("")
//         setLyrics("")
//     }
//
//     useEffect(() => {
//         if (!playingTrack) return
//
//         axios
//             .get("http://localhost:3001/lyrics", {
//                 params: {
//                     track: playingTrack.title,
//                     artist: playingTrack.artist,
//                 },
//             })
//             .then(res => {
//                 setLyrics(res.data.lyrics)
//             })
//     }, [playingTrack])
//
//     useEffect(() => {
//         if (!accessToken) return
//         spotifyApi.setAccessToken(accessToken)
//     }, [accessToken])
//
//     useEffect(() => {
//         if (!search) return setSearchResults([])
//         if (!accessToken) return
//
//         let cancel = false
//         spotifyApi.searchTracks(search).then(res => {
//             if (cancel) return
//             setSearchResults(
//                 res.body.tracks.items.map(track => {
//                     const smallestAlbumImage = track.album.images.reduce(
//                         (smallest, image) => {
//                             if (image.height < smallest.height) return image
//                             return smallest
//                         },
//                         track.album.images[0]
//                     )
//
//                     return {
//                         artist: track.artists[0].name,
//                         title: track.name,
//                         uri: track.uri,
//                         albumUrl: smallestAlbumImage.url,
//                     }
//                 })
//             )
//         })
//
//         return () => (cancel = true)
//     }, [search, accessToken])
//
//     return (
//         <Container className="d-flex flex-column py-2" style={{ height: "100vh" }}>
//             <Form.Control
//                 type="search"
//                 placeholder="Search Songs/Artists"
//                 value={search}
//                 onChange={e => setSearch(e.target.value)}
//             />
//             <div className="flex-grow-1 my-2" style={{ overflowY: "auto" }}>
//                 {searchResults.map(track => (
//                     <TrackSearchResult
//                         track={track}
//                         key={track.uri}
//                         chooseTrack={chooseTrack}
//                     />
//                 ))}
//                 {searchResults.length === 0 && (
//                     <div className="text-center" style={{ whiteSpace: "pre" }}>
//                         {lyrics}
//                     </div>
//                 )}
//             </div>
//             <div>
//                 <Player accessToken={accessToken} trackUri={playingTrack?.uri} />
//             </div>
//         </Container>
//     )
// }

// placeholders aren’t accessible. By adding a label, we can tell screen reader users what the input field is for.
const SearchBar = () => (
    <div className="search_bar">
        <form action="/" method="get">
            <label htmlFor="header-search">
                <span className="visually-hidden">Search blog posts</span>
            </label>
            <input className = "bar"
                   type="text"
                   id="header-search"
                   placeholder="Search Songs/Albums"
                   name="s"
            />

            <button className="submit_button" type="submit">Search</button>
        </form>
    </div>
);

export default SearchBar;


// const spotifyApi = new SpotifyWebApi({
//     clientId: "b56736cc5cf944718d0bf12378798dbc",
// })
//
// // placeholders aren’t accessible. By adding a label, we can tell screen reader users what the input field is for.
// function SearchBar() {
//     const [search, setSearch] = useState('')
//     const [searchResults, setSearchResults] = useState([])
//
//     let access_token = global.access_token
//
//     useEffect(() => {
//         if (!access_token) return
//         spotifyApi.setAccessToken(access_token)
//     }, [access_token])
//
//     useEffect(() => {
//         if (!search) return setSearchResults([])
//         if (!access_token) return
//
//         spotifyApi.searchTracks(search).then(res => {
//             console.log(res)
//         })
//     }, [search, access_token])
//
//     return (
//         <Container className="d-flex flex-column py-2" style={{height: "100vh"}}>
//             <Form.Control
//                 type="search"
//                 placeholder="search Songs/Albums"
//                 value={search}
//                 onChange={e => setSearch(e.target.value)}
//             />
//             <div className="flex-grow-1 my-2" style={{ overflowY: "auto"}}>Results</div>
//             <div>Bottom</div>
//         </Container>
//     )
// };

// export default Search;

