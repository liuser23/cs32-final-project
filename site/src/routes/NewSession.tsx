import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import '../App.css';
import axios from 'axios';
import SideBar from "../SideBar";
import {Async, useFetch} from "react-async";
import {useLocation, useNavigate, useParams} from "react-router-dom";

type UserData = {
    displayName: string,
    followerCount: number,
    imageUrl: string,
}

function NewSession(props: {setSessionToken: Dispatch<SetStateAction<string>>}) {
    const sessionToken = new URLSearchParams(useLocation().search).get('sessionToken') ?? ''
    console.log('setting token to ' + sessionToken)

    const navigate = useNavigate()

    useEffect(() => {
        props.setSessionToken(sessionToken)
        navigate('/')
    })

    return <h1>Logging you in...</h1>
}
//
// function Authorized() {
//
//     if (data) {
//     } else {
//         return <button>Error: Could not fetch page </button>
//     }
// }

export default NewSession;