import React, {useEffect, useState} from 'react';
import '../App.css';
import axios from 'axios';
import DefaultPfp from '../images/PngItem_1503945.png';
import SideBar from "../SideBar";
import ProfilePhoto from "../ProfilePhoto";
import {Link} from "react-router-dom";

type image = {
    height : number;
    url : string;
    width : number;
}

type artistBySong = {
    externalUrls : { externalUrls : {spotify : string} };
    href : string;
    id : string;
    name : string;
    type : string;
    uri : string;
}

type artist = {
    externalUrls : { externalUrls : {spotify : string} };
    followers: { total : number };
    genre : string[];
    href : string;
    id : string;
    images : image[];
    name : string;
    popularity : number;
    type : string;
    uri : string;
}

type albumBySong = {
    albumType : string;
    artists : artistBySong[];
    availableMarkets: string[];
    externalUrls : { externalUrls : {spotify : string} };
    href : "string";
    id : string;
    images : image[];
    name : string;
    type : string;
    uri : string;
}

type track = {
    album : albumBySong;
    artists : artistBySong[];
    availableMarkets : string;
    discNumber : number;
    durationMs : number;
    explicit : boolean;
    externalIds : { externalIds : {isrc : string} };
    externalUrls : { externalUrls : {spotify : string} };
    href : string;
    id : string;
    name : string;
    popularity : number;
    previewUrl : string;
    trackNumber : number;
    type : string;
    uri : string;
}

function Authorized(props : {userPfp : string, setPfp : (uPfp : string) => void}) {



    const [topSongs, setTopSongs] = useState<track[]>([]);
    const [topArtists, setTopArtists] = useState<artist[]>([]);



    const [curUserName, setCurUserName] = useState<string>("loading...");
    const [numFollowers, setNumFollowers] = useState<number>(0);

    const [topSong1, setTopSong1] = useState<string>("");
    const [topSongImg1, setTopSongImg1] = useState<string>("loading...");
    const [topSongUrl1, setTopSongUrl1] = useState<string>("");

    const [topSong2, setTopSong2] = useState<string>("");
    const [topSongImg2, setTopSongImg2] = useState<string>("loading...");
    const [topSongUrl2, setTopSongUrl2] = useState<string>("");

    const [topSong3, setTopSong3] = useState<string>("");
    const [topSongImg3, setTopSongImg3] = useState<string>("loading...");
    const [topSongUrl3, setTopSongUrl3] = useState<string>("");

    const [topSong4, setTopSong4] = useState<string>("");
    const [topSongImg4, setTopSongImg4] = useState<string>("loading...");
    const [topSongUrl4, setTopSongUrl4] = useState<string>("");


    const [topArtist1, setTopArtist1] = useState<string>("");
    const [topArtistImg1, setTopArtistImg1] = useState<string>("loading...");
    const [topArtist2, setTopArtist2] = useState<string>("");
    const [topArtistImg2, setTopArtistImg2] = useState<string>("loading...");
    const [topArtist3, setTopArtist3] = useState<string>("");
    const [topArtistImg3, setTopArtistImg3] = useState<string>("loading...");
    const [topArtist4, setTopArtist4] = useState<string>("");
    const [topArtistImg4, setTopArtistImg4] = useState<string>("loading...");

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

    async function getTopTracks(props : {sSong1 : (curSong : string) => void, sImg1 : (songImg : string) => void,
        sUrl1 : (curUrl : string) => void,
        sSong2 : (curSong : string) => void, sImg2 : (songImg : string) => void,
        sUrl2 : (curUrl : string) => void,
        sSong3 : (curSong : string) => void, sImg3 : (songImg : string) => void,
        sUrl3 : (curUrl : string) => void,
        sSong4 : (curSong : string) => void, sImg4 : (songImg : string) => void,
        sUrl4 : (curUrl : string) => void,
        sTracks : (curTracks : track[]) => void}) {
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

                props.sSong1(response.data[0].name)
                props.sImg1(response.data[0].album.images[0].url)
                props.sUrl1(response.data[0].previewUrl)

                props.sSong2(response.data[1].name)
                props.sImg2(response.data[1].album.images[0].url)
                props.sUrl2(response.data[1].previewUrl)

                props.sSong3(response.data[2].name)
                props.sImg3(response.data[2].album.images[0].url)
                props.sUrl3(response.data[2].previewUrl)

                props.sSong4(response.data[3].name)
                props.sImg4(response.data[3].album.images[0].url)
                props.sUrl4(response.data[3].previewUrl)
            })
    }

    async function getTopArtists(props : {sArtist1 : (curArtist : string) => void, sImg1 : (artistImg : string) => void,
        sArtist2 : (curArtist : string) => void, sImg2 : (artistImg : string) => void,
        sArtist3 : (curArtist : string) => void, sImg3 : (artistImg : string) => void,
        sArtist4 : (curArtist : string) => void, sImg4 : (artistImg : string) => void,
        sArtists : (curArtists : artist[]) => void}) {
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

                props.sArtist1(response.data[0].name)
                props.sImg1(response.data[0].images[0].url)
                props.sArtist2(response.data[1].name)
                props.sImg2(response.data[1].images[0].url)
                props.sArtist3(response.data[2].name)
                props.sImg3(response.data[2].images[0].url)
                props.sArtist4(response.data[3].name)
                props.sImg4(response.data[3].images[0].url)
            })
    }

    useEffect(() => {
        //getUserName({change : setCurUserName})
        getUserData({sName : setCurUserName, sFollowers : setNumFollowers, sPfp : props.setPfp})
        getTopTracks({sSong1 : setTopSong1, sImg1 : setTopSongImg1,
            sUrl1 : setTopSongUrl1,
            sSong2 : setTopSong2, sImg2 : setTopSongImg2,
            sUrl2 : setTopSongUrl2,
            sSong3 : setTopSong3, sImg3 : setTopSongImg3,
            sUrl3 : setTopSongUrl3,
            sSong4 : setTopSong4, sImg4 : setTopSongImg4,
            sUrl4 : setTopSongUrl4,
            sTracks : setTopSongs});
        getTopArtists({sArtist1 : setTopArtist1, sImg1 : setTopArtistImg1,
            sArtist2 : setTopArtist2, sImg2 : setTopArtistImg2,
            sArtist3 : setTopArtist3, sImg3 : setTopArtistImg3,
            sArtist4 : setTopArtist4, sImg4 : setTopArtistImg4,
            sArtists : setTopArtists});
    });

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
                <p><>Top Song: {topSong1}</></p>
                <br/>
                <p><>Top Songs</></p>
                <div className={"Topsongs-box"}>
                    <div className={"Song-box"}>
                        <a href={topSongUrl1} className={"Song-art"}>
                            <img className={"Song-art"} src={topSongs[0].album.images[0].url}/>
                        </a>
                        {topSong1}
                    </div>
                    <div className={"Song-box"}>
                        <a href={topSongUrl2} className={"Song-art"}>
                            <img className={"Song-art"} src={topSongImg2}/>
                        </a>
                        {topSong2}
                    </div>
                    <div className={"Song-box"}>
                        <a href={topSongUrl3} className={"Song-art"}>
                            <img className={"Song-art"} src={topSongImg3}/>
                        </a>
                        {topSong3}
                    </div>
                    <div className={"Song-box"}>
                        <a href={topSongUrl4} className={"Song-art"}>
                            <img className={"Song-art"} src={topSongImg4}/>
                        </a>
                        {topSong4}
                    </div>
                </div>
                <br/>
                <p><>Top Artists</></p>
                <div className={"Topsongs-box"}>
                    <div className={"Song-box"}>
                        <img className={"Song-art"} src={topArtistImg1}/>
                        {topArtist1}
                    </div>
                    <div className={"Song-box"}>
                        <img className={"Song-art"} src={topArtistImg2}/>
                        {topArtist2}
                    </div>
                    <div className={"Song-box"}>
                        <img className={"Song-art"} src={topArtistImg3}/>
                        {topArtist3}
                    </div>
                    <div className={"Song-box"}>
                        <img className={"Song-art"} src={topArtistImg4}/>
                        {topArtist4}
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Authorized;