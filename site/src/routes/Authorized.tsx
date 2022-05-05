import React, {useEffect, useState} from 'react';
import '../App.css';
import axios from 'axios';
import DefaultPfp from '../images/PngItem_1503945.png';
import SideBar from "../SideBar";
import ProfilePhoto from "../ProfilePhoto";
import {Link} from "react-router-dom";
import TopSongsBox from "../TopSongsBox";
import TopArtistsBox from "../TopArtistsBox";

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
    href : string;
    id : string;
    images : image[];
    name : string;
    type : string;
    uri : string;
}

type track = {
    album : albumBySong;
    artists : artistBySong[];
    availableMarkets : string[];
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

    const defaultString : string = "loading..."
    const defaultImageHeight : number = 200;
    const defaultImageWidth : number = 200;
    const defaultImageUrl : string = "../images/default_album_art.png";
    const defaultNumber : number = 1;
    const defaultBoolean : boolean = false;
    const [curUserName, setCurUserName] = useState<string>("loading...");
    const [numFollowers, setNumFollowers] = useState<number>(0);


    function makeDefaultArtistBySong() {
        let defArtist : artistBySong = {externalUrls : {externalUrls : {spotify : defaultString}},
            href : defaultString, id : defaultString, name : defaultString, type : defaultString, uri : defaultString};
        return defArtist;
    }

    function makeDefaultImage() {
        let defImage : image = {height : defaultImageHeight, url : defaultImageUrl, width : defaultImageWidth};
        return defImage;
    }

    function makeDefaultAlbumBySong() {
        let defAlbum : albumBySong = {albumType : defaultString, artists : [makeDefaultArtistBySong()],
            availableMarkets : [defaultString], externalUrls : {externalUrls : {spotify : defaultString}},
            href : defaultString, id : defaultString, images : [makeDefaultImage()], name : defaultString,
            type : defaultString, uri : defaultString};
        return defAlbum;
    }

    function makeDefaultTrack() {
        let defTrack : track = {album : makeDefaultAlbumBySong(), artists : [makeDefaultArtistBySong()],
            availableMarkets : [defaultString], discNumber : defaultNumber, durationMs : defaultNumber,
            explicit : defaultBoolean, externalIds : {externalIds : {isrc : defaultString}},
            externalUrls : {externalUrls : {spotify : defaultString}}, href : defaultString, id : defaultString,
            name : defaultString, popularity : defaultNumber, previewUrl : defaultString, trackNumber : defaultNumber,
            type : defaultString, uri : defaultString};
        return defTrack;
    }

    function makeDefaultArtist() {
        let defArtist : artist = {externalUrls : {externalUrls : {spotify : defaultString}},
            followers : {total : defaultNumber}, genre : [defaultString], href : defaultString, id : defaultString,
            images : [makeDefaultImage()], name : defaultString, popularity : defaultNumber, type : defaultString,
            uri : defaultString};
        return defArtist;
    }



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