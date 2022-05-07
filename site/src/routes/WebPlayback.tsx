import React, { useState, useEffect } from 'react';
import {Authentication} from "../App";

const track = {
    name: "",
    album: {
        images: [
            { url: "" }
        ]
    },
    artists: [
        { name: "" }
    ]
}

type Player = {
    addListener: (event: string, thing: {device_id: string}) => void,
    connect: () => void,
    previousTrack: () => void,
    togglePlay: () => void,
    nextTrack: () => void,
}

function WebPlayback(props: {authentication: Authentication}) {
    const [is_paused, setPaused] = useState(false);
    const [is_active, setActive] = useState(false);
    const [player, setPlayer] = useState<Player>();
    const [current_track, setTrack] = useState(track);

    useEffect(() => {
        const script = document.createElement("script");
        script.src = "https://sdk.scdn.co/spotify-player.js";
        script.async = true;

        document.body.appendChild(script);

        // @ts-ignore
        console.log('start use event', window.onSpotifyWebPlaybackSDKReady)

        // @ts-ignore
        window.onSpotifyWebPlaybackSDKReady = () => {

            console.log('playback ready')
            // @ts-ignore
            const player = new window.Spotify.Player({
                name: 'Web Playback SDK',
                // @ts-ignore
                getOAuthToken: cb => cb(props.authentication.accessToken),
                volume: 0.5
            });

            setPlayer(player);

            // @ts-ignore
            player.addListener('ready', ({ device_id }) => {
                console.log('Ready with Device ID', device_id);
            });

            // @ts-ignore
            player.addListener('not_ready', ({ device_id }) => {
                console.log('Device ID has gone offline', device_id);
            });

            // @ts-ignore
            player.addListener('player_state_changed', ( state => {
                if (!state) {
                    return;
                }

                setTrack(state.track_window.current_track);
                setPaused(state.paused);

                // @ts-ignore
                player.getCurrentState().then(s => setActive(!!s));
            }));

            player.connect();

        };
    }, []);


    if (player && is_active) {
        const previousTrack = () => player.previousTrack()
        const togglePlay = () => player.togglePlay()
        const nextTrack = () => player.nextTrack()

        return (
            <>
                <div className="container">
                    <div className="main-wrapper">

                        <img src={current_track.album.images[0].url} className="now-playing__cover" alt="" />

                        <div className="now-playing__side">
                            <div className="now-playing__name">{current_track.name}</div>
                            <div className="now-playing__artist">{current_track.artists[0].name}</div>

                            <button className="btn-spotify" onClick={previousTrack} >
                                &lt;&lt;
                            </button>

                            <button className="btn-spotify" onClick={togglePlay} >
                                { is_paused ? "PLAY" : "PAUSE" }
                            </button>

                            <button className="btn-spotify" onClick={nextTrack} >
                                &gt;&gt;
                            </button>
                        </div>
                    </div>
                </div>
            </>
        );
    } else {
        console.log(player, is_active)
        return (
            <>
                <div className="container">
                    <div className="main-wrapper">
                        <b> Instance not active. Transfer your playback using your Spotify app </b>
                    </div>
                </div>
            </>
        )

    }
}

export default WebPlayback
