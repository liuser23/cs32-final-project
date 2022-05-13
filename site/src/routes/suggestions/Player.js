import React, {useEffect, useState} from 'react'
import SpotifyPlayer from "react-spotify-web-playback"

export default function Player({accessToken, trackUri}) {
    const [play, setPlay] = useState(false)

    useEffect(() => setPlay(true), [trackUri])

    if (!accessToken) return null
    return (
        <SpotifyPlayer style={{
            activeColor: '#fff',
            backgroundColor: '#1DB954',
            sliderColor: '#1DB954',
        }}
        token={accessToken}
        showSaveIcon
        callback={state => {
            if (!state.isPlaying) setPlay(false)
        }}
        play={play}
        magnifySliderOnHover={true}
        uris={trackUri ? [trackUri] : []}
        />
    )
}