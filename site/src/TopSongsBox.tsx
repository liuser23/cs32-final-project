import React, {useEffect, useState} from 'react';
import './App.css';
import axios from 'axios';
import DefaultPfp from './images/PngItem_1503945.png';
import SideBar from "./SideBar";

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

function TopSongsBox (props : {topSongs : track[]}) {

    let index : number = 0;

    return (
        <div className={"Topsongs-box"}>
            {props.topSongs.slice(index,index+4).map(x => <div className={"Song-box"}>
                <a href={x.previewUrl} className={"Song-art"}>
                    <img className={"Song-art"} src={x.album.images[0].url}/>
                </a>
                {x.name}
            </div>)}
        </div>
    );
}

export default TopSongsBox;