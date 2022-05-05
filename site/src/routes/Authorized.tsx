import React, {useEffect, useState} from 'react';
import '../App.css';
import axios from 'axios';
import DefaultPfp from '../images/PngItem_1503945.png';
import SideBar from "../SideBar";
import ProfilePhoto from "../ProfilePhoto";
import {Link} from "react-router-dom";
import TopSongsBox from "../TopSongsBox";
import TopArtistsBox from "../TopArtistsBox";
import {track, artist} from "../MyTypes";

function Authorized(props : {userPfp : string, setPfp : (uPfp : string) => void}) {

    const [curUserName, setCurUserName] = useState<string>("loading...");
    const [numFollowers, setNumFollowers] = useState<number>(0);
    const [topSongs, setTopSongs] = useState<track[]>([]);
    const [topArtists, setTopArtists] = useState<artist[]>([]);

    async function getUserData(props : {sName : (curName : string) => void,
        sFollowers : (curFollowers : number) => void, sPfp : (curPfp : string) => void}) {
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        await axios.get("http://localhost:8888/userData", config)
            .then(response => {
                console.log("response2", response.data)
                props.sName(response.data.displayName)
                props.sFollowers(response.data.followers.total)
                props.sPfp(response.data.images[0].url)
            })
    }

    async function getTopTracks(props : {sTracks : (curTracks : track[]) => void}) {
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        await axios.get("http://localhost:8888/topTracks", config)
            .then(response => {
                console.log("Top Songs", response.data)
                props.sTracks(response.data)
            })
    }

    async function getTopArtists(props : {sArtists : (curArtists : artist[]) => void}) {
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        await axios.get("http://localhost:8888/topArtists", config)
            .then(response => {
                console.log("Top Artists:", response.data)
                props.sArtists(response.data)
            })
    }

    useEffect(() => {
        //getUserName({change : setCurUserName})
        getUserData({sName : setCurUserName, sFollowers : setNumFollowers, sPfp : props.setPfp})
        getTopTracks({sTracks : setTopSongs});
        getTopArtists({sArtists : setTopArtists});
    }, []);

    return(
        <div>
            <SideBar pfp={props.userPfp}/>
            <div className={"Main-window"}>
                <p>Success! You have logged in.</p>
                <p><>Name: {curUserName}</></p>
                <p><>Followers: {numFollowers}</></p>
                <br/>
                <br/>
                <br/>
                <br/>
                <p><>Top Songs</></p>
                <TopSongsBox topSongs={topSongs}/>
                <br/>
                <p><>Top Artists</></p>
                <TopArtistsBox topArtists={topArtists}/>
            </div>
        </div>
    )
}

export default Authorized;