import React, { useState, useEffect, useRef } from 'react';
import useAuth from './useAuth'
import SpotifyWebApi from "spotify-web-api-node"
import TrackSearchResult from "./TrackSearchResult";

const spotifyApi = new SpotifyWebApi({
    clientId: "786d7d05062645e09e6cc0f9df174325",
})

function TodoForm(props) {
    const accessToken = useAuth(props.code)
    const [search, setSearch] = useState("")
    const [searchResults, setSearchResults] = useState([] )
    let searchArray = [];

    const [input, setInput] = useState(props.edit ? props.edit.value : '');

    const inputRef = useRef(null);

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

    useEffect(() => {
        inputRef.current.focus();
    });

    const handleChange = e => {
        setInput(e.target.value);
    };

    const handleSubmit = e => {
        e.preventDefault()
        props.onSubmit({
            id: Math.floor(Math.random() * 10000),
            text: input
        });
        setInput('');
    };

    return (
        <form onSubmit={handleSubmit} className='todo-form'>
                    <input
                        placeholder='Add Song Recommendation'
                        value={search}
                        onChange={handleChange}
                        //onChange={e => setSearch(e.target.value)}
                        name='text'
                        className='todo-input'
                        ref={inputRef}
                        size="50"

                    />
                    <button onClick={handleSubmit} className='todo-button'>
                        Add Rec
                    </button>
        </form>
    );
}

export default TodoForm;