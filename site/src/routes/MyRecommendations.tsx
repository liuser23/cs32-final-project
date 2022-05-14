import React, {useEffect, useState} from 'react';
import '../App.css';
import RecommenderInput from "../RecommenderInput";
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import {Button, Card, CardActions, CardContent, Typography} from "@mui/material";
import {Authentication} from "../App";
import axios from "axios";
import {currentUser} from "../MyTypes"

function MyRecommendations(props: {authentication: Authentication}) {

    const [isClicked, setIsClicked] = useState<boolean>(false);
    const [recType, setRecType] = useState<number | number[]>(1);
    const [songWeight, setSongWeight] = useState<number | number[]>(50);
    const [artistWeight, setArtistWeight] = useState<number | number[]>(50);
    const [genreWeight, setGenreWeight] = useState<number | number[]>(50);

    const [recOutput, setRecOutput] = useState<currentUser[]>([]);

    async function getRecommendations() {
        setIsClicked(true)
        console.log("button clicked!")
        console.log("Is clicked: " + isClicked)
        console.log("Rec type: " + recType)
        console.log("Song weight: " + songWeight)
        console.log("Artist weight: " + artistWeight)
        console.log("Genre weight: " + genreWeight)
        let mSame : boolean = true;
        if (recType === 0) {
            mSame = false;
        }
        const config = {
            headers: {
                'Authentication': props.authentication.sessionToken,
                "Content-Type": "application/json",
                'Access-Control-Allow-Origin': '*',
            }
        }

        const postData = {
            data: {
                "songs": {matchSame : mSame, matchWeight : songWeight},
                "genres": {matchSame : mSame, matchWeight : genreWeight},
                "artists": {matchSame : mSame, matchWeight : artistWeight},
            }
        }

        await axios.post(process.env.REACT_APP_POST_CREATE_RECS as string, postData, config).then(
            response => {console.log(response)}
        );

        await axios.get(process.env.REACT_APP_GET_RECS as string, config)
            .then(
                response => {
                    console.log(response.data)
                    setRecOutput(response.data)
                },
                reason => console.log(reason),
            );
    }

    return (
        <div className={"Main-window"}>
            <div className={"Recommendations-header"}>
                My Recommendations
            </div>
            <hr className = {"Profile-horizontal-line"}/>
            <RecommenderInput title={"Find users whose music taste is..."} type={0} val={recType} updateValue={setRecType}/>
            <hr className = {"Recommender-divider"}/>
            <RecommenderInput title={"Importance of songs: "} type={1} val={songWeight} updateValue={setSongWeight}/>
            <hr className = {"Recommender-divider"}/>
            <RecommenderInput title={"Importance of artists: "} type={1} val={artistWeight} updateValue={setArtistWeight}/>
            <hr className = {"Recommender-divider"}/>
            <RecommenderInput title={"Importance of genres: "} type={1} val={genreWeight} updateValue={setGenreWeight}/>
            <hr className = {"Recommender-divider"}/>
            <Button variant="outlined" color={"secondary"} onClick={getRecommendations}>Get recommendations</Button>
            <br/>
            {recOutput.map(x => <div>Display Name: {x.displayName} User ID: {x.id}</div>)}
        </div>
    )
}

export default MyRecommendations;