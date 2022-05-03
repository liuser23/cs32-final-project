import React, {useEffect, useState} from 'react';
import '../App.css';
import SideBar from "../SideBar";
import DefaultPfp from '../images/PngItem_1503945.png';


function ChangePassword() {

    return (
        <div>
            <div className={"Side-bar"}>
                <SideBar pfp={DefaultPfp}/>
            </div>
            <div className={"Main-window"}>
                Change Password
            </div>
        </div>
    )
}

export default ChangePassword;