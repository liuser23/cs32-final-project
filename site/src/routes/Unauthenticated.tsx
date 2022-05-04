import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import '../App.css';
import axios from 'axios';
import {useLocation} from "react-router-dom";

function Unauthenticated() {
    async function getAuthorization() {
        let config = {
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
            }
        }
        await axios.get('http://localhost:8888/login', config)
            .then(response => window.location.href = response.data.redirect)
    }

    return (
        <>
            <h1>Welcome to the best Spotify social media platform</h1>
            <p>Take your first step toward making new friends by signing in with your existing Spotify account</p>
            <button onClick={getAuthorization}>Log in</button>
        </>
    )
}

export default Unauthenticated;