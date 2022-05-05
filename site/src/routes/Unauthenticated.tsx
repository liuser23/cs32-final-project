import React from 'react';
import '../App.css';

function Unauthenticated() {
    console.log()

    return (
        <>
            <h1>Welcome to the best Spotify social media platform</h1>
            <p>Take your first step toward making new friends by signing in with your existing Spotify account</p>
            <form action={process.env.REACT_APP_LOGIN_REDIRECT}>
                <button type='submit'>Log in</button>
            </form>
        </>
    )
}

export default Unauthenticated;