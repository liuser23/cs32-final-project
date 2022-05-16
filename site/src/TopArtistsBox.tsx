import React, {useEffect, useState} from 'react';
import './App.css';
import {artist} from "./MyTypes";
import DefaultAlbum from './images/default_album_art.png';

function TopArtistsBox(props : {topArtists : artist[]}) {

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
        <div id="topArtistsBox" className={"Topsongs-box"}>
            <p style={{paddingTop: "15px"}} className={"Top-title"}>Top Artists</p>
                <button id={"songNavButtonLeft"} className={"Top-navbutton-left"} onClick={decrementIndex}></button>
                <div id={"topArtistsList"} className={"Top-items-list"}>
                    {props.topArtists.slice(index,index+4).map(x => <div id="artistBox" className={"Song-box"}>
                        <img className={"Song-art"} src={x.images[0]?.url ?? DefaultAlbum} alt={`album cover of ` + x.name}/>
                        {x.name}
                    </div>)}
                </div>
                <button id={"songNavButtonRight"} className={"Top-navbutton-right"} onClick={incrementIndex}></button>
        </div>
    );
}

export default TopArtistsBox;