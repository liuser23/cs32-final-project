import React, {useEffect, useState} from 'react';


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
    images : image[];
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

type currentUser = {
    country : string;
    displayName : string;
    email : string;
    externalUrls : { externalUrls : {spotify : string} };
    followers : {total : number};
    href : string;
    id : string;
    images : image[];
    product : string;
    type : string;
    uri : string;
}

export type {image, artistBySong, artist, albumBySong, track, currentUser};