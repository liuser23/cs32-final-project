import React, {useEffect, useState} from 'react';
import '../App.css';
import axios from 'axios';
import DefaultPfp from '../images/PngItem_1503945.png';
import SideBar from "../SideBar";
import ProfilePhoto from "../ProfilePhoto";

function Authorized() {



    const [curUserName, setCurUserName] = useState<string>("loading...");
    const [numFollowers, setNumFollowers] = useState<number>(0);
    const [pfp, setPfp] = useState<string>(DefaultPfp);

    async function getUserData(props : {sName : (curName : string) => void,
        sFollowers : (curFollowers : number) => void, sPfp : (curPfp : string) => void}) {
        let config = {
            headers: {
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }
        await axios.get("http://localhost:8888/userData", config)
            .then(response => {
                console.log("response2", response.data)
                props.sName(response.data.displayName)
                props.sFollowers(response.data.followers.total)
                props.sPfp(response.data.images[0].url)
            })
    }

    useEffect(() => {
        //getUserName({change : setCurUserName})
        getUserData({sName : setCurUserName, sFollowers : setNumFollowers, sPfp : setPfp})
    });

    return(
        <div>
            <SideBar pfp={pfp}/>
            <div className={"Main-window"}>
                <p>Success! You have logged in.</p>
                <p><>Name: {curUserName}</></p>
                <p><>Followers: {numFollowers}</></p>
            </div>
        </div>
    )
}

export default Authorized;