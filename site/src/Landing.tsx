import React, {useState} from 'react';
import './App.css';
import axios from 'axios';

const LOGIN_ENDPOINT = '/login'

function Landing() {

    const clicked = () => {
        console.log('clicked');
    };

    const [authCodeUri, setAuthCodeUri] = useState<string>("");

    async function getAuthorization(props : {change : (codeUri : string) => void}) {
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        await axios.get("http://localhost:8888/login", config)
            .then(response => {
                console.log("response", response.data);
                props.change(response.data); // set authCode
                window.location.href = response.data;
            })
    }

    return(
        <div>
            <a href={LOGIN_ENDPOINT}>Login in hello to Spotify</a>
            <button onClick={async () => await getAuthorization({change : setAuthCodeUri})}>Click me</button>
        </div>
    )
}

export default Landing;