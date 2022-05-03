import React, {useEffect, useState} from 'react';
import '../App.css';
import SideBar from "../SideBar";
import DefaultPfp from "../images/PngItem_1503945.png";

function MyRecommendations(props : {userPfp : string}) {

    return (
        <div>
            <div className={"Side-bar"}>
                <SideBar pfp={props.userPfp}/>
            </div>
            <div className={"Main-window"}>
                My Recomendations
            </div>
        </div>
    )
}

export default MyRecommendations;