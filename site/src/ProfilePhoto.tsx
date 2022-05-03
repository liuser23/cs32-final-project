import React, {useEffect, useState} from 'react';
import './App.css';

function ProfilePhoto( props : {image : string}) {

    return (
        <div className={"Pfp-container"}>
            <img src={props.image} className={"Pfp-image"} alt={"Your profile photo!"}/>
        </div>
    )
}

export default ProfilePhoto;