import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import './App.css';
import {track} from "./MyTypes";
import DefaultAlbum from './images/default_album_art.png';


function TopSongsBox (props : {topSongs : track[], setNowPlaying : (nowPlaying : track | undefined) => void}) {

    const [index, setIndex] = useState<number>(0);

    function incrementIndex() {
        if (index < 16) {
            setIndex(index + 1);
        }
    }

    function decrementIndex() {
        if (index > 0) {
            setIndex(index -1);
        }
    }

    return (
        <div id="topSongsBox" className={"Topsongs-box"}>
            <p style={{paddingTop: "15px"}}className={"Top-title"}>Top Songs</p>
            <button id={"songNavButtonLeft"} className={"Top-navbutton-left"} onClick={decrementIndex}></button>
            <div id={"topSongsList"} className={"Top-items-list"}>
                {props.topSongs.slice(index,index+4).map(x => <div key={x.uri} id="songBox" className={"Song-box"}>
                        <img className={"Song-art"} src={x.album.images[0]?.url ?? DefaultAlbum} onClick={() => props.setNowPlaying(x)} alt={`album cover of ` + x.name}/>
                    {x.name}
                </div>)}
            </div>
            <button id={"songNavButtonRight"} className={"Top-navbutton-right"} onClick={incrementIndex}></button>
        </div>
    );
}

export default TopSongsBox;