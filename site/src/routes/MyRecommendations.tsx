import React, {useState} from 'react';
import '../App.css';
import RecommenderInput from "../RecommenderInput";
import Box from '@mui/material/Box';
import Paper from '@mui/material/Paper';
import {Button, Card, CardActions, CardContent, Typography} from "@mui/material";

function MyRecommendations() {

    return (
        <div className={"Main-window"}>
            <div className={"Recommendations-header"}>
                My Recommendations
            </div>
            <hr className = {"Profile-horizontal-line"}/>
            <RecommenderInput title={"Find users whose music taste is..."} type={0}/>
            <hr className = {"Recommender-divider"}/>
            <RecommenderInput title={"Importance of songs: "} type={1}/>
            <hr className = {"Recommender-divider"}/>
            <RecommenderInput title={"Importance of artists: "} type={1}/>
            <hr className = {"Recommender-divider"}/>
            <RecommenderInput title={"Importance of genres: "} type={1}/>
            <hr className = {"Recommender-divider"}/>
        </div>
    )
}

export default MyRecommendations;