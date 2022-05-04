import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import '../App.css';
import axios from 'axios';
import {useLocation} from "react-router-dom";

function Unauthenticated() {
    return (
        <>
            <h1>Welcome to the best Spotify social media platform</h1>
            <p>Take your first step toward making new friends by signing in with your existing Spotify account</p>
            <form action='http://localhost:8888/login'>
                <button type='submit'>Log in</button>
            </form>
        </>
    )
}

export default Unauthenticated;