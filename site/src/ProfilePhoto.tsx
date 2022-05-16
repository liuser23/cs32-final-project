import React, {useEffect, useState} from 'react';
import './App.css';

function ProfilePhoto( props : {image : string}) {

    return (
        <div className={"Pfp-container"} id="profilePic">
            <img src={props.image} style={{objectFit: 'contain'}} className={"Pfp-image"} alt={"Your profile photo!"}/>
        </div>
    )
}

export default ProfilePhoto;