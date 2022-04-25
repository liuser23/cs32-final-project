import React from 'react';
import './App.css';

const LOGIN_ENDPOINT = '/login'

function Landing() {

    const clicked = () => {
        console.log('clicked');
    };

    return(
        <div>
            <a href={LOGIN_ENDPOINT}>Login in to Spotify</a>
            <button onClick={clicked}>Click me</button>
        </div>
    )
}

export default Landing;