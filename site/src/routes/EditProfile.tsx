import React, {useEffect, useState} from 'react';
import '../App.css';
import SideBar from "../SideBar";
import DefaultPfp from "../images/PngItem_1503945.png";

function EditProfile() {

    return (
        <div>
            <div className={"Side-bar"}>
                <SideBar pfp={DefaultPfp}/>
            </div>
            <div className={"Main-window"}>
                Edit Profile
            </div>
        </div>
    )
}

export default EditProfile;