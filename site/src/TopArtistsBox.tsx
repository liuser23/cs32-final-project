import React, {useEffect, useState} from 'react';
import './App.css';
import {artist} from "./MyTypes";


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