import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import '../App.css';
import axios from 'axios';
import DefaultPfp from '../images/PngItem_1503945.png';
import SideBar from "../SideBar";
import ProfilePhoto from "../ProfilePhoto";
import {Link} from "react-router-dom";
import '../App.css';
import {SidebarConfig} from "../App";

type UserData = {
    displayName: string,
    followerCount: number,
    imageUrl: string,
}

type TopSong = {
    name: string,
    image: string,
    url: string,
}

type TopArtist = {
    name: string,
    image: string,
}

function Home(props: {sessionToken: string, sidebarConfig: SidebarConfig, setSidebarConfig: Dispatch<SetStateAction<SidebarConfig>>}) {
    const [error, setError] = useState<string>()
    const [userData, setUserData] = useState<UserData>()
    const [topSongs, setTopSongs] = useState<TopSong[]>([])
    const [topArtists, setTopArtists] = useState<TopArtist[]>()

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
        fetch('http://localhost:8888/topTracks', config)
            .then(result => result.json())
            .then(
                (result) => setTopSongs(result),
                (error) => setError(error),
            )
        fetch('http://localhost:8888/topArtists', config)
            .then(result => result.json())
            .then(
                (result) => setTopArtists(result),
                (error) => setError(error),
            )
    }, [])

    if (userData && topSongs && topArtists) {
        return (
            <>
            <SideBar pfp={props.sidebarConfig.profilePicturePath}/>
            <div className={"Main-window"}>
                <p>Success! You have logged in.</p>
                <p><>Name: {userData.displayName}</></p>
                <p><>Followers: {userData.followerCount}</></p>
                <br/>
                <br/>
                <br/>
                <br/>
                <div className={"Topsongs-box"}>
                    <h2>Top Songs</h2>
                    <>
                        {topSongs.map((topSong) => {
                                <div className={"Song-box"}>
                                    <a href={topSong.url} className={"Song-art"}>
                                        <img className={"Song-art"} src={topSong.image}/>
                                    </a>
                                    {topSong.name}
                                </div>
                            })}
                    </>
                </div>
                <br/>
                <div className={"Topsongs-box"}>
                    <h2>Top Artists</h2>
                    <>
                        {topArtists.map((topArtist) => {
                                <div className={"Song-box"}>
                                    <img className={"Song-art"} src={topArtist.image}/>
                                    {topArtist.name}
                                </div>
                            })}
                    </>
                </div>
            </div>
        </>
        )

    } else if (error) {
        return <pre>Error: {error}</pre>
    } else {
        return <h1>Loading...</h1>
    }

}

export default Home;