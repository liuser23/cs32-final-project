import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import '../App.css';
import axios from 'axios';
import DefaultPfp from '../images/PngItem_1503945.png';
import SideBar from "../SideBar";
import '../App.css';
import {SidebarConfig} from "../App";
import {artist, track} from "../MyTypes";
import TopSongsBox from "../TopSongsBox";
import TopArtistsBox from "../TopArtistsBox";

function Home(props: {sessionToken: string, setSidebarConfig: Dispatch<SetStateAction<SidebarConfig>>}) {
    const [error, setError] = useState<string>()
    const [curUserName, setCurUserName] = useState<string>();
    const [numFollowers, setNumFollowers] = useState<number>();
    const [topSongs, setTopSongs] = useState<track[]>();
    const [topArtists, setTopArtists] = useState<artist[]>();

    const config = {
        headers: {
            'Authentication': props.sessionToken,
            "Content-Type": "application/json",
            'Access-Control-Allow-Origin': '*',
        }
    }

    const getUserData = async () =>
        await axios.get(process.env.REACT_APP_USER_DATA_ENDPOINT as string, config)
            .then(
                response => {
                    setCurUserName(response.data.displayName)
                    setNumFollowers(response.data.followers.total)
                    props.setSidebarConfig({profilePicturePath : response.data.images[0].url})
                },
                reason => setError(reason),
            )

    const getTopTracks = async () =>
        await axios.get(process.env.REACT_APP_TOP_TRACKS_ENDPOINT as string, config)
            .then(
                response => setTopSongs(response.data),
                reason => setError(reason),
            )

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
    }, []);

    return (
        <div>
            <div className={"Main-window"}>
                { curUserName !== undefined && numFollowers !== undefined ?
                    <><p>Hello {curUserName}</p><p>Followers: {numFollowers}</p></> :
                    <p>Loading Loading Profile Info</p>
                }
                { error !== undefined ? <p>Error: {error}</p> : <></> }
                { (topArtists !== undefined && topArtists.length !== 0) ?
                    <><p>Top Artists</p><TopArtistsBox topArtists={topArtists}/></> :
                    <></>
                }
                <br/>
                <br/>
                { topSongs !== undefined && topSongs.length !== 0 ?
                    <><p>Top Songs</p><TopSongsBox topSongs={topSongs}/></> :
                    <></>
                }
                <br/>
                <br/>
            </div>
        </div>
    )
}

export default Home;
