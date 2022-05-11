import React, {useEffect, useState} from 'react';
import '../App.css';
import RecommenderInput from "../RecommenderInput";
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import {Button, Card, CardActions, CardContent, Typography} from "@mui/material";

function MyRecommendations() {

    const [isClicked, setIsClicked] = useState<boolean>(false);
    const [recType, setRecType] = useState<number | number[]>(0);
    const [songWeight, setSongWeight] = useState<number | number[]>(50);
    const [artistWeight, setArtistWeight] = useState<number | number[]>(50);
    const [genreWeight, setGenreWeight] = useState<number | number[]>(50);
    const [results, setResults] = useState<JSX.Element>(<></>);

    function getRecommendations() {
        setIsClicked(true)
        console.log("button clicked!")
        console.log("Is clicked: " + isClicked)
        console.log("Rec type: " + recType)
        console.log("Song weight: " + songWeight)
        console.log("Artist weight: " + artistWeight)
        console.log("Genre weight: " + genreWeight)
        setResults(<div>nice, results! rt: {recType} sw: {songWeight} aw: {artistWeight} gw: {genreWeight}</div>)
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
            {results}
        </div>
    )
}

export default MyRecommendations;