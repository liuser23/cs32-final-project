import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import '../App.css';
import axios from 'axios';
import DefaultPfp from '../images/PngItem_1503945.png';
import SideBar from "../SideBar";
import '../App.css';
import {Authentication, SidebarConfig} from "../App";
import {artist, artistBySong, track} from "../MyTypes";
import TopSongsBox from "../TopSongsBox";
import TopArtistsBox from "../TopArtistsBox";
import ProfileHeader from "../ProfileHeader";

function Home(props: {authentication: Authentication, setSidebarConfig: Dispatch<SetStateAction<SidebarConfig>>,
    setNowPlaying: Dispatch<SetStateAction<track | undefined>>}) {
    const [error, setError] = useState<string>()
    const [curUserName, setCurUserName] = useState<string>();
    const [numFollowers, setNumFollowers] = useState<number>();
    const [topSongs, setTopSongs] = useState<track[]>();
    const [topArtists, setTopArtists] = useState<artist[]>();
    const [topGenres, setTopGenres] = useState<Map<string,number>>(new Map<string,number>());
    let otherTopGenres : Map<string,number> = new Map<string,number>();

    const config = {
        headers: {
            'Authentication': props.authentication.sessionToken,
            "Content-Type": "application/json",
            'Access-Control-Allow-Origin': '*',
        }
    }

    const getUserData = async () =>
        await axios.get(process.env.REACT_APP_USER_DATA_ENDPOINT as string, config)
            .then(
                response => {
                    console.log(response.data)
                    setCurUserName(response.data.displayName)
                    setNumFollowers(response.data.followers.total)
                    props.setSidebarConfig({profilePicturePath : response.data.images[0]?.url})
                },
                reason => setError(reason),
            )

    const getTopTracks = async () => {
        await axios.get(process.env.REACT_APP_TOP_TRACKS_ENDPOINT as string, config)
            .then(
                response => {
                    setTopSongs(response.data);

                },
                reason => setError(reason),
            );
    }

    const getTopArtists = async () =>
        await axios.get(process.env.REACT_APP_TOP_ARTISTS_ENDPOINT as string, config)
            .then(
                response => setTopArtists(response.data),
                reason => setError(reason),
            )



    useEffect(() => {
        getUserData()
        getTopTracks()
        getTopArtists()
    }, [])

    return (
            <div className={"Main-window"} style={{top:'40px'}}>
                <div style={{paddingBottom: '30px'}}>
                { curUserName !== undefined && numFollowers !== undefined ?
                    <ProfileHeader username={curUserName}/> :
                    <p>Loading Loading Profile Info</p>
                }
                </div>
                <hr className = {"Profile-horizontal-line"}/>
                { error !== undefined ? <p>Error: {error}</p> : <></> }
                { (topArtists !== undefined && topArtists.length !== 0) ?
                    <><TopArtistsBox topArtists={topArtists}/></> :
                    <></>
                }
                <br/>
                { topSongs !== undefined && topSongs.length !== 0 ?
                    <><TopSongsBox topSongs={topSongs} setNowPlaying={props.setNowPlaying}/></> :
                    <></>
                }
                <br/>
                <br/>
            </div>
    )
}

export default Home;
