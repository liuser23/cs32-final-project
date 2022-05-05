import React, {useEffect, useState} from 'react';
import './App.css';
import {track} from "./MyTypes";
import DefaultAlbum from './images/default_album_art.png';


function TopSongsBox (props : {topSongs : track[]}) {
    let index : number = 0;

    return (
        <div className={"Topsongs-box"}>
            {props.topSongs.slice(index,index+4).map(x => <div className={"Song-box"}>
                <a href={x.previewUrl} className={"Song-art"}>
                    <img className={"Song-art"} src={x.album.images[0]?.url ?? DefaultAlbum} alt={`album cover of ` + x.name}/>
                </a>
                {x.name}
            </div>)}
        </div>
    );
}

export default TopSongsBox;