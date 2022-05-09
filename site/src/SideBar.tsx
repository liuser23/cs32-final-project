import React, {useEffect, useState} from 'react';
import './App.css';

import DefaultPfp from './images/PngItem_1503945.png';
import AccMenuButton from "./AccMenuButton";
import PicSignOut from './images/basebuttons/sign-out 1.png'
import PicHome from './images/basebuttons/home 1.png'
import PicLock from './images/basebuttons/lock 1.png'
import PicPencil from './images/basebuttons/pencil 1.png'
import PicFriends from './images/basebuttons/friends 1.png'
import PicRecList from './images/basebuttons/list-format 1.png'
import PicBell from './images/basebuttons/notifications 1.png'
import ProfilePhoto from "./ProfilePhoto";
import {Authentication, SidebarConfig} from "./App";
import SpotifyPlayer from "react-spotify-web-playback";

function SideBar(props: {nowPlaying: string | undefined, authentication: Authentication, sidebarConfig: SidebarConfig}) {
    console.log(props.nowPlaying)
    return (
        <div className={"Side-bar"}>
            <ProfilePhoto image={props.sidebarConfig.profilePicturePath ?? DefaultPfp}/>
            {props.nowPlaying !== undefined ?
                <SpotifyPlayer
                    uris={[props.nowPlaying]}
                    token={props.authentication.accessToken}
                />
                : <></>
            }

            <div className={"Account-menu"}>
                <AccMenuButton picture={PicHome} picAlt={"house"} txtContent={"Account Overview"} route={"/"}/>
                <AccMenuButton picture={PicLock} picAlt={"lock"} txtContent={"Search"} route={"/search"}/>
                <AccMenuButton picture={PicLock} picAlt={"lock"} txtContent={"Privacy Settings"} route={"/privacysettings"}/>
                <AccMenuButton picture={PicPencil} picAlt={"pencil"} txtContent={"Edit Profile"} route={"/editprofile"}/>
                <AccMenuButton picture={PicLock} picAlt={"lock"} txtContent={"Change Password"} route={"/changepassword"}/>
                <AccMenuButton picture={PicBell} picAlt={"bell"} txtContent={"Notification Settings"} route={"/notificationsettings"}/>
                <AccMenuButton picture={PicFriends} picAlt={"friends"} txtContent={"Friends List"} route={"/friendslist"}/>
                <AccMenuButton picture={PicRecList} picAlt={"list"} txtContent={"My Recommendations"} route={"/myrecommendations"}/>
                <AccMenuButton picture={PicSignOut} picAlt={"exit"} txtContent={"Sign Out"} route={"/signout"}/>
            </div>
        </div>
    )
}

export default SideBar;