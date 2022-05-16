import React, {Dispatch, SetStateAction, useEffect, useState} from 'react';
import './App.css';

function ProfileHeader(props : {username : string}) {

    return(
        <div className={"Profile-header"}>
            <p id="userNameText" className={"User-name"}>{props.username}</p>
        </div>
    );
}

export default ProfileHeader;