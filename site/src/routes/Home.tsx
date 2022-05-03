import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import '../App.css';
import axios from 'axios';
import SideBar from "../SideBar";
import {Async, useFetch} from "react-async";
import {useLocation} from "react-router-dom";

type UserData = {
    displayName: string,
    followerCount: number,
    imageUrl: string,
}

function Home(props: {sessionToken: string}) {
    const [userData, setUserData] = useState<UserData>()
    const [error, setError] = useState<string>()

    useEffect(() => {
        let config = {
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                'Authentication': props.sessionToken,
            }
        }

        fetch('http://localhost:8888/userData', config)
            .then(result => result.json())
            .then(
                (result) => setUserData(result),
                (error) => setError(error),
            )
    }, [])

    if (userData) {
        return (
            <div>
                <SideBar pfp={userData.imageUrl}/>
                <div className={"Main-window"}>
                    <p>Success! You have logged in.</p>
                    <p>Name: {userData.displayName}</p>
                    <p>Followers: {userData.followerCount}</p>
                </div>
            </div>
        )
    } else if (error) {
        return <pre>Error: {error}</pre>
    } else {
        return <p>Loading...</p>
    }
}

export default Home;