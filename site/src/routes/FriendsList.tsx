import React, {useEffect, useState} from 'react';
import '../App.css';
import SideBar from "../SideBar";
import DefaultPfp from "../images/PngItem_1503945.png";
import {SidebarConfig} from "../App";

function FriendsList(props: {sidebarConfig: SidebarConfig}) {

    return (
        <div>
            <div className={"Side-bar"}>
                <SideBar pfp={props.sidebarConfig.profilePicturePath}/>
            </div>
            <div className={"Main-window"}>
                Friends List
            </div>
        </div>
    )
}

export default FriendsList;