import React, {useEffect, useState} from 'react';
import '../App.css';
import SideBar from "../SideBar";
import DefaultPfp from "../images/PngItem_1503945.png";
import {SidebarConfig} from "../App";

function SignOut(props: {sidebarConfig: SidebarConfig}) {
    return (
        <div>
            <div className={"Side-bar"}>
                <SideBar sidebarConfig={props.sidebarConfig}/>
            </div>
            <div className={"Main-window"}>
                Sign Out
            </div>
        </div>
    )
}

export default SignOut;