import React from 'react';
import { Container } from "react-bootstrap"

const AUTH_URL =
    "https://accounts.spotify.com/authorize?client_id=b56736cc5cf944718d0bf12378798dbc&response_type=code&redirect_uri=http://localhost:3000&scope=streaming%20user-read-email%20user-read-private%20user-library-read%20user-library-modify%20user-read-playback-state%20user-modify-playback-state"

const OLD_AUTH_URL = "https://accounts.spotify.com/authorize?client_id=b56736cc5cf944718d0bf12378798dbc&response_type=code&redirect_uri=http://localhost:3000/auth/callback&scope=streaming%20user-read-email%20user-read-private%20user-library-read%20user-library-modify%20user-read-playback-state%20user-modify-playback-state"


// export default function Login() {
//     return (
//         <Container
//             className="d-flex justify-content-center align-items-center"
//             style={{ minHeight: "100vh" }}
//         >
//             <a className="btn btn-success btn-lg" href={AUTH_URL}>
//                 Login With Spotify
//             </a>
//         </Container>
//     )
// }
function Login() {
    return (
        <div className="App">
            <header className="App-header">
                <a className="btn-spotify" href="/auth/login" >
                    Login with Spotify
                </a>
            </header>
            <SearchBar></SearchBar>
        </div>
    );
}

export default Login;

