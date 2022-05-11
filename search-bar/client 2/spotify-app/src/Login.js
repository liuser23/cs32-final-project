import React from 'react'
import { Container } from 'react-bootstrap'

// const AUTH_URL = "https://accounts.spotify.com/authorize?client_id=&response_type=code&redirect_uri=http://localhost:3000&scope=%20"
const AUTH_URL = "https://accounts.spotify.com/authorize?client_id=786d7d05062645e09e6cc0f9df174325&response_type=code&redirect_uri=http://localhost:3000&scope=streaming%20user-read-email%20user-read-private%20user-library-read%20user-library-modify%20user-read-playback-state%20user-modify-playback-state%20playlist-modify%20playlist-modify-private%20playlist-modify-public%20playlist-read-private"

// const AUTH_URL =   "https://accounts.spotify.com/authorize?client_id=786d7d05062645e09e6cc0f9df174325&response_type=code&redirect_uri=http://localhost:3000/callback/&scope=streaming%20user-read-email%20user-read-private%20user-library-read%20user-library-modify%20user-read-playback-state%20user-modify-playback-state"
export default function Login() {
    return (
        <Container
            className="d-flex justify-content-center align-items-center"
            style={{ minHeight: "100vh" }}
        >
            <a className="btn btn-success btn-lg" href={AUTH_URL}>
                Login With Spotify
            </a>
        </Container>
    )
}