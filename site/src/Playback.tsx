import React, {useEffect, useState} from 'react';
import './App.css';
import SpotifyPlayer from 'react-spotify-web-playback';

import {Authentication} from "./App";

function Playback(props: {authentication: Authentication}) {
    return (
        <SpotifyPlayer
            token={props.authentication.accessToken}
            uris={['spotify:artist:6HQYnRM4OzToCYPpVBInuU']}
        />
    )
}

export default Playback;