import React, {useEffect, useState} from 'react';
import './App.css';

type image = {
    height : number;
    url : string;
    width : number;
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


function TopArtistsBox(props : {topArtists : artist[]}) {

    let index : number = 0;

    return (
        <div className={"Topsongs-box"}>
            {props.topArtists.slice(index,index+4).map(x => <div className={"Song-box"}>
                <img className={"Song-art"} src={x.images[0].url}/>
                {x.name}
            </div>)}
        </div>
    );
}

export default TopArtistsBox;