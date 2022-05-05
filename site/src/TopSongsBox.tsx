import React, {useEffect, useState} from 'react';
import './App.css';
import axios from 'axios';
import DefaultPfp from './images/PngItem_1503945.png';
import SideBar from "./SideBar";
import {track} from "./MyTypes";

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